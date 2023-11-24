package io.github.imsejin.study.springframework.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ConditionalNextStepJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    Job conditionalNextStepJob() {
        return new JobBuilder("conditionalNextStepJob", jobRepository)
                .start(firstStep())
                /**/.on(ExitStatus.FAILED.getExitCode()) // If firstStep failed
                /**/.to(endStep()) // Moves to endStep
                /**/.on("*") // Regardless of result of endStep
                /**/.end() // Flow ends
                .from(firstStep()) // From firstStep (like event listener)
                /**/.on("*") // Regardless of result of firstStep (on this context, when it is not failed)
                /**/.to(successStep()) // Moves to successStep
                /**/.next(endStep()) // When successStep is succeeded, moves to endStep (if next replaced with start, the step doesn't work)
                /**/.on("*") // Regardless of result of endStep
                /**/.end() // Flow ends
                .end() // Job ends
                .build();
    }

    @Bean
    Step firstStep() {
        return new StepBuilder("firstStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== conditionalNextStepJob.firstStep");
                    // Regards this step as failed step.
                    contribution.setExitStatus(ExitStatus.FAILED);

                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    @Bean
    Step successStep() {
        return new StepBuilder("successStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== conditionalNextStepJob.successStep");
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    @Bean
    Step endStep() {
        return new StepBuilder("endStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== conditionalNextStepJob.endStep");
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

}
