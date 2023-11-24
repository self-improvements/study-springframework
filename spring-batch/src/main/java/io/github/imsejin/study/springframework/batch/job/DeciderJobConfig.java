package io.github.imsejin.study.springframework.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Random;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DeciderJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    Job deciderJob() {
        return new JobBuilder("deciderJob", jobRepository)
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
        return new StepBuilder("startStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== deciderJob.{}", contribution.getStepExecution().getStepName());
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    @Bean
    Step oddStep() {
        return new StepBuilder("oddStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== deciderJob.{}", contribution.getStepExecution().getStepName());
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    @Bean
    Step evenStep() {
        return new StepBuilder("evenStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== deciderJob.{}", contribution.getStepExecution().getStepName());
                    return RepeatStatus.FINISHED;
                }), transactionManager)
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
