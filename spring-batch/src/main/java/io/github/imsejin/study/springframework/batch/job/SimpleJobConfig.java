package io.github.imsejin.study.springframework.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SimpleJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    Job simpleJob() {
        return jobBuilderFactory.get("simpleJob")
                .start(simpleStep0())
                .next(simpleStep1())
                .build();
    }

    @Bean
    Step simpleStep0() {
        return stepBuilderFactory.get("simpleStep0")
                .tasklet(((contribution, chunkContext) -> {
                    log.info("===== Step 1: {}, {}", contribution, chunkContext);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    Step simpleStep1() {
        return stepBuilderFactory.get("simpleStep1")
                .tasklet(((contribution, chunkContext) -> {
                    log.info("===== Step 2: {}, {}", contribution, chunkContext);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

}
