package io.github.imsejin.study.springframework.batch.job;

import java.util.UUID;

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

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ExecutionContextRestartJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    Job executionContextRestartJob() {
        return new JobBuilder("executionContextRestartJob", jobRepository)
                .start(executionContextRestartJob$storingStateStep())
                .next(executionContextRestartJob$accessPreviousStepStateStep())
                .next(executionContextRestartJob$reloadStoredStateStep())
                .next(executionContextRestartJob$lastStep())
                .build();
    }

    @Bean
    Step executionContextRestartJob$storingStateStep() {
        return new StepBuilder("storingStateStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    var jobExecutionContext = contribution.getStepExecution().getJobExecution().getExecutionContext();
                    if (!jobExecutionContext.containsKey("jobName")) {
                        var jobName = contribution.getStepExecution().getJobExecution().getJobInstance().getJobName();
                        jobExecutionContext.put("jobName", jobName);
                    }

                    log.debug("contribution.stepExecution.jobExecution.executionContext['jobName']: {}",
                            jobExecutionContext.get("jobName"));

                    var stepExecutionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();
                    if (!stepExecutionContext.containsKey("stepName")) {
                        var stepName = chunkContext.getStepContext().getStepExecution().getStepName();
                        stepExecutionContext.put("stepName", stepName);
                    }

                    log.debug("chunkContext.stepContext.stepExecution.executionContext['stepName']: {}",
                            stepExecutionContext.get("stepName"));

                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    @Bean
    Step executionContextRestartJob$accessPreviousStepStateStep() {
        return new StepBuilder("accessPreviousStepStateStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    var jobExecutionContext = chunkContext.getStepContext().getJobExecutionContext();
                    if (!jobExecutionContext.containsKey("jobName")) {
                        throw new IllegalStateException("NEVER THROW");
                    }

                    var stepExecutionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();
                    if (stepExecutionContext.containsKey("stepName")) {
                        throw new IllegalStateException("NEVER THROW");
                    }

                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    @Bean
    Step executionContextRestartJob$reloadStoredStateStep() {
        return new StepBuilder("reloadStoredStateStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    var jobExecutionContext = chunkContext.getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();
                    var jobUid = jobExecutionContext.get("uid");

                    if (jobUid == null) {
                        jobExecutionContext.put("uid", UUID.randomUUID());
                        throw new IllegalStateException("ONCE THROW: Not found jobUid");
                    }

                    var stepExecutionContext = contribution.getStepExecution().getExecutionContext();
                    var stepUid = stepExecutionContext.get("uid");

                    if (stepUid == null) {
                        stepExecutionContext.put("uid", UUID.randomUUID());
                        throw new IllegalStateException("ONCE THROW: Not found stepUid");
                    }

                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    @Bean
    Step executionContextRestartJob$lastStep() {
        return new StepBuilder("lastStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    var stepExecutionContext = contribution.getStepExecution().getExecutionContext();

                    // State is stored into the database though this step will be succeeded.
                    stepExecutionContext.put("lastStep", true);

                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

}
