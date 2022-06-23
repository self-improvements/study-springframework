package io.github.imsejin.study.springframework.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
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
public class ConditionalNextStepJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    Job conditionalNextStepJob() {
        return jobBuilderFactory.get("conditionalNextStepJob")
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
        return stepBuilderFactory.get("firstStep")
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== conditionalNextStepJob.firstStep");
                    // Regards this step as failed step.
                    contribution.setExitStatus(ExitStatus.FAILED);

                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    Step successStep() {
        return stepBuilderFactory.get("successStep")
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== conditionalNextStepJob.successStep");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    Step endStep() {
        return stepBuilderFactory.get("endStep")
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== conditionalNextStepJob.endStep");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

}
