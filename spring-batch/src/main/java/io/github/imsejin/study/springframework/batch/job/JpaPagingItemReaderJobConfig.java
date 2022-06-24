package io.github.imsejin.study.springframework.batch.job;

import io.github.imsejin.study.springframework.batch.entity.KanClassificationEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JpaPagingItemReaderJobConfig {

    private static final int CHUNK_SIZE = 100;

    private static final String JOB_NAME = "jpa_paging_item_reader_job";

    private static final String STEP_NAME = "jpa_paging_item_reader_step";

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;

    @Bean(JOB_NAME)
    Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step())
                .build();
    }

    @Bean(STEP_NAME)
    Step step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<KanClassificationEntity, KanClassificationEntity>chunk(CHUNK_SIZE)
                .reader(jpaPagingItemReader())
                .writer(jpaPagingItemWriter())
                .build();
    }

    @Bean
    JpaPagingItemReader<KanClassificationEntity> jpaPagingItemReader() {
        return new JpaPagingItemReaderBuilder<KanClassificationEntity>()
                .pageSize(CHUNK_SIZE)
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT k FROM KanClassificationEntity k WHERE code LIKE CONCAT(:code, '%')")
                .parameterValues(Map.of("code", "01"))
                .name("jpa_paging_item_reader")
                .build();
    }

    ItemWriter<KanClassificationEntity> jpaPagingItemWriter() {
        return items -> {
            for (var item : items) {
                log.debug(">>>>> item: {}", item);
            }
        };
    }

}
