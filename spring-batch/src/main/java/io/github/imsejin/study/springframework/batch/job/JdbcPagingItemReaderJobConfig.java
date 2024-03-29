package io.github.imsejin.study.springframework.batch.job;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
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
public class JdbcPagingItemReaderJobConfig {

    private static final int CHUNK_SIZE = 100;
    private static final String JOB_NAME = "jdbc_paging_item_reader_job";
    private static final String STEP_NAME = "jdbc_paging_item_reader_step";

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
                .reader(jdbcPagingItemReader())
                .writer(jdbcPagingItemWriter())
                .build();
    }

    @Bean
    JdbcPagingItemReader<KanClassification> jdbcPagingItemReader() {
        return new JdbcPagingItemReaderBuilder<KanClassification>()
                .pageSize(CHUNK_SIZE)
                .fetchSize(CHUNK_SIZE)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(KanClassification.class))
                .queryProvider(successfulQueryProvider())
                //                .queryProvider(failedQueryProvider())
                .parameterValues(Map.of("code", "01"))
                .name("jdbc_paging_item_reader")
                .build();
    }

    ItemWriter<KanClassification> jdbcPagingItemWriter() {
        return items -> {
            for (KanClassification item : items) {
                log.debug(">>>>> item: {}", item);
            }
        };
    }

    @Bean
    PagingQueryProvider successfulQueryProvider() {
        SqlPagingQueryProviderFactoryBean factoryBean = new SqlPagingQueryProviderFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setSelectClause("""
                SELECT T.code
                     , T.name
                     , T.level
                     , T.parentCode
                     , T.parentLevel
                     , T.description
                """);
        factoryBean.setFromClause("""
                    FROM (
                        SELECT KAN_CODE AS code
                             , CLS_NM AS name
                             , OPP_CLS_LVL_NO AS level
                             , KAN_PRT_CODE AS parentCode
                             , PRT_CLS_LVL_NO AS parentLevel
                             , KAN_DESC AS description
                        FROM KAN_CLS
                        WHERE DLT_YN = 'N'
                        AND USE_YN = 'Y'
                        AND KAN_CODE LIKE CONCAT(:code, '%')
                        ) T
                """);
        factoryBean.setSortKeys(Map.of("code", Order.ASCENDING));

        try {
            return factoryBean.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Due to {@code JdbcPagingItemReader$PagingRowMapper.mapRow()}.
     *
     * @see <a href="https://github.com/spring-projects/spring-batch/issues/1741">
     * JdbcPagingItemReader does not support table or column aliases
     * due to sortKey being used in where clause, order by clause and
     * for retrieval of result set column [BATCH-1848]</a>
     */
    @Bean
    PagingQueryProvider failedQueryProvider() {
        SqlPagingQueryProviderFactoryBean factoryBean = new SqlPagingQueryProviderFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setSelectClause("""
                SELECT KAN_CODE AS code
                     , CLS_NM AS name
                     , OPP_CLS_LVL_NO AS level
                     , KAN_PRT_CODE AS parentCode
                     , PRT_CLS_LVL_NO AS parentLevel
                     , KAN_DESC AS description
                """);
        factoryBean.setFromClause("FROM KAN_CLS");
        factoryBean.setWhereClause("""
                WHERE DLT_YN = 'N'
                AND USE_YN = 'Y'
                AND KAN_CODE LIKE CONCAT(:code, '%')
                """);
        factoryBean.setSortKeys(Map.of("KAN_CODE", Order.ASCENDING));
        //        factoryBean.setSortKeys(Map.of("code", Order.ASCENDING));

        try {
            return factoryBean.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
