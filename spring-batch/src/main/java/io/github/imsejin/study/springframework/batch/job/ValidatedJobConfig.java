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

import io.github.imsejin.study.springframework.batch.job.validator.NotEmptyJobParametersValidator;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ValidatedJobConfig {

    public static final String JOB_NAME = "validatedJob";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean(JOB_NAME)
    Job job() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(step1())
                .next(step2())
                .validator(new NotEmptyJobParametersValidator())
                //                .validator(new DefaultJobParametersValidator(new String[] {"version"}, new String[] {"sequence"}))
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

    @Bean(JOB_NAME + "step2")
    Step step2() {
        return new StepBuilder(JOB_NAME + "step2", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info("===== Step 2: {}, {}", contribution, chunkContext);
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

}
