package io.github.imsejin.study.springframework.batch.job;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.github.imsejin.study.springframework.batch.domain.KanClassification;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JdbcCursorItemReaderJobConfig {

    private static final int CHUNK_SIZE = 100;
    private static final String JOB_NAME = "jdbc_cursor_item_reader_job";
    private static final String STEP_NAME = "jdbc_cursor_item_reader_step";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;

    @Bean(JOB_NAME)
    Job job() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(step())
                .build();
    }

    @Bean(STEP_NAME)
    Step step() {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<KanClassification, KanClassification>chunk(CHUNK_SIZE, transactionManager)
                .reader(reader())
                .writer(writer())
                .build();
    }

    /**
     * CursorItemReader를 사용할 때는 DB와 SocketTimeout을 충분히 큰 값으로 설정해야 합니다.
     * 기본적으로 TCP 통신은 Socket으로 하기 때문에 타임아웃을 설정해줘야 합니다.
     * Cursor는 하나의 Connection으로 Batch가 끝날 때까지 사용되기 때문에
     * 작업이 다 끝나기전에 DB와 애플리케이션의 Connection이 먼저 끊어질 수 있습니다.
     *
     * <p> Batch 수행 시간이 오래 걸리는 경우에는 PagingItemReader를 사용하는 게 낫습니다.
     * Paging의 경우 한 페이지를 읽을때마다 Connection을 맺고 끊기 때문에
     * 아무리 많은 데이터라도 타임아웃과 부하 없이 수행될 수 있습니다.
     */
    JdbcCursorItemReader<KanClassification> reader() {
        return new JdbcCursorItemReaderBuilder<KanClassification>()
                .fetchSize(CHUNK_SIZE)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(KanClassification.class))
                .sql("""
                        SELECT KAN_CODE AS code
                             , CLS_NM AS name
                             , OPP_CLS_LVL_NO AS level
                             , KAN_PRT_CODE AS parentCode
                             , PRT_CLS_LVL_NO AS parentLevel
                             , KAN_DESC AS description
                        FROM KAN_CLS
                        WHERE DLT_YN = 'N'
                        AND USE_YN = 'Y'
                        ORDER BY KAN_CODE
                        LIMIT 500
                        """)
                .name("jdbc_cursor_item_reader")
                .build();
    }

    ItemWriter<KanClassification> writer() {
        return items -> {
            for (KanClassification item : items) {
                log.debug(">>>>> item: {}", item);
            }
        };
    }

}
