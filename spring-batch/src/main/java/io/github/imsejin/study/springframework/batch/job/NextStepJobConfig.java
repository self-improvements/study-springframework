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
public class NextStepJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    Job nextStepJob() {
        return jobBuilderFactory.get("nextStepJob")
                .start(step0())
                .next(step1())
                .next(step2())
                .build();
    }

    @Bean
    Step step0() {
        return stepBuilderFactory.get("step0")
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== Step 0: {}, {}", contribution, chunkContext);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== Step 1: {}, {}", contribution, chunkContext);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== Step 2: {}, {}", contribution, chunkContext);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

}
