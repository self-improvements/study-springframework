package io.github.imsejin.study.springframework.batch.job;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
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

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SimpleJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    Job simpleJob() {
        return new JobBuilder("simpleJob", jobRepository)
                .start(simpleStep0())
                .next(simpleStep1())
                .build();
    }

    @Bean
    Step simpleStep0() {
        return new StepBuilder("simpleStep0", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info("===== Step 1: {}, {}", contribution, chunkContext);

                    // contribution.stepExecution.jobExecution.jobParameters
                    JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();
                    for (Map.Entry<String, JobParameter<?>> entry : jobParameters.getParameters().entrySet()) {
                        JobParameter<?> jobParameter = entry.getValue();
                        System.out.printf("contribution.stepExecution.jobExecution.jobParameters['%s']: %s(%s, %s)%n",
                                entry.getKey(),
                                jobParameter.getClass().getSimpleName(),
                                jobParameter.getType(),
                                jobParameter.getValue());
                    }

                    // chunkContext.stepContext.jobParameters
                    Map<String, Object> jobParamMap = chunkContext.getStepContext().getJobParameters();
                    for (Map.Entry<String, Object> entry : jobParamMap.entrySet()) {
                        Object jobParamValue = entry.getValue();
                        System.out.printf("chunkContext.stepContext.jobParameters['%s']: %s(%s)%n",
                                entry.getKey(), jobParamValue.getClass().getSimpleName(), jobParamValue);
                    }

                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    @Bean
    Step simpleStep1() {
        return new StepBuilder("simpleStep1", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info("===== Step 2: {}, {}", contribution, chunkContext);
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

}
