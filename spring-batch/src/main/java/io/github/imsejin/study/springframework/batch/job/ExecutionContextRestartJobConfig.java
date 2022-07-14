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

import java.util.UUID;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ExecutionContextRestartJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    Job executionContextRestartJob() {
        return jobBuilderFactory.get("executionContextRestartJob")
                .start(executionContextRestartJob$storingStateStep())
                .next(executionContextRestartJob$accessPreviousStepStateStep())
                .next(executionContextRestartJob$reloadStoredStateStep())
                .next(executionContextRestartJob$lastStep())
                .build();
    }

    @Bean
    Step executionContextRestartJob$storingStateStep() {
        return stepBuilderFactory.get("storingStateStep")
                .tasklet(((contribution, chunkContext) -> {
                    var jobExecutionContext = contribution.getStepExecution().getJobExecution().getExecutionContext();
                    if (!jobExecutionContext.containsKey("jobName")) {
                        var jobName = contribution.getStepExecution().getJobExecution().getJobInstance().getJobName();
                        jobExecutionContext.put("jobName", jobName);
                    }

                    log.debug("contribution.stepExecution.jobExecution.executionContext['jobName']: {}", jobExecutionContext.get("jobName"));

                    var stepExecutionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();
                    if (!stepExecutionContext.containsKey("stepName")) {
                        var stepName = chunkContext.getStepContext().getStepExecution().getStepName();
                        stepExecutionContext.put("stepName", stepName);
                    }

                    log.debug("chunkContext.stepContext.stepExecution.executionContext['stepName']: {}", stepExecutionContext.get("stepName"));

                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    Step executionContextRestartJob$accessPreviousStepStateStep() {
        return stepBuilderFactory.get("accessPreviousStepStateStep")
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
                }))
                .build();
    }

    @Bean
    Step executionContextRestartJob$reloadStoredStateStep() {
        return stepBuilderFactory.get("reloadStoredStateStep")
                .tasklet(((contribution, chunkContext) -> {
                    var jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
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
                }))
                .build();
    }

    @Bean
    Step executionContextRestartJob$lastStep() {
        return stepBuilderFactory.get("lastStep")
                .tasklet(((contribution, chunkContext) -> {
                    var stepExecutionContext = contribution.getStepExecution().getExecutionContext();

                    // State is stored into the database though this step will be succeeded.
                    stepExecutionContext.put("lastStep", true);

                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

}
