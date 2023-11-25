package io.github.imsejin.study.springframework.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.github.imsejin.study.springframework.batch.job.incrementer.TimestampIncrementer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class IncrementedJobConfig {

    public static final String JOB_NAME = "incrementedJob";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean(JOB_NAME)
    Job job() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(step1())
                .incrementer(new TimestampIncrementer())
                .build();
    }

    @Bean(JOB_NAME + "step1")
    Step step1() {
        return new StepBuilder(JOB_NAME + "step1", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info("===== Step 1: {}, {}", contribution, chunkContext);
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

}
