package io.github.imsejin.study.springframework.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DeciderJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    Job deciderJob() {
        return jobBuilderFactory.get("deciderJob")
                .start(startStep())
                .next(decider())
                .from(decider())
                /**/.on("ODD")
                /**/.to(oddStep())
                .from(decider())
                /**/.on("EVEN")
                /**/.to(evenStep())
                .end()
                .build();
    }

    @Bean
    Step startStep() {
        return stepBuilderFactory.get("startStep")
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== deciderJob.{}", contribution.getStepExecution().getStepName());
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    Step oddStep() {
        return stepBuilderFactory.get("oddStep")
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== deciderJob.{}", contribution.getStepExecution().getStepName());
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    Step evenStep() {
        return stepBuilderFactory.get("evenStep")
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== deciderJob.{}", contribution.getStepExecution().getStepName());
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    JobExecutionDecider decider() {
        return (jobExecution, stepExecution) -> {
            var random = new Random();

            var n = random.nextInt(100) + 1;

            log.debug("===== Random number: {}", n);

            var status = (n & 1) == 1 ? "ODD" : "EVEN";
            return new FlowExecutionStatus(status);
        };
    }

}
