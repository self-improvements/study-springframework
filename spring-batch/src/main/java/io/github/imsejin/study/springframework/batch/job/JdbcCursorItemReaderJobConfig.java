package io.github.imsejin.study.springframework.batch.job;

import io.github.imsejin.study.springframework.batch.domain.KanClassification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.Locale;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JdbcCursorItemReaderJobConfig {

    private static final int CHUNK_SIZE = 100;

    // jdbc_cursor_item_reader_job
    private final String JOB_NAME = getClass().getSimpleName().replaceAll("(?<!^|_|[A-Z])([A-Z])", "_$1")
            .replaceAll("^(.+_Job).*$", "$1").toLowerCase(Locale.US);

    // jdbc_cursor_item_reader_step
    private final String STEP_NAME = getClass().getSimpleName().replaceAll("(?<!^|_|[A-Z])([A-Z])", "_$1")
            .replaceAll("^(.+)_Job.*$", "$1_step").toLowerCase(Locale.US);

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final DataSource dataSource;

    @Bean
    Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step())
                .build();
    }

    @Bean
    Step step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<KanClassification, KanClassification>chunk(CHUNK_SIZE)
                .reader(reader())
                .writer(writer())
                .build();
    }

    /**
     * CursorItemReader를 사용할 때는 DB와 SocketTimeout을 충분히 큰 값으로 설정해야 합니다.
     * 기본적으로 TCP 통신은 Socket으로 하기 때문에 타임아웃을 설정해줘야 합니다.
     * Cursor는 하나의 Connection으로 Batch가 끝날 때까지 사용되기 때문에
     * 작업이 다 끝나기전에 DB와 애플리케이션의 Connection이 먼저 끊어질 수 있습니다.
     */
    @Bean
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

    @Bean
    ItemWriter<KanClassification> writer() {
        return items -> {
            for (var item : items) {
                log.debug(">>>>> item: {}", item);
            }
        };
    }

}
