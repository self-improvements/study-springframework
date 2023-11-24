package io.github.imsejin.study.springframework.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
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
class FlowJob1Config {

    public static final String JOB_NAME = "flowJob1";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean(JOB_NAME)
    Job job() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(flow())
                .next(step3())
                .end()
                .build();
    }

    @Bean(JOB_NAME + ":flow")
    Flow flow() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>(JOB_NAME + ":flow");
        flowBuilder.start(step1())
                .next(step2())
                .end();

        return flowBuilder.build();
    }

    @Bean(JOB_NAME + ":step1")
    Step step1() {
        return new StepBuilder(JOB_NAME + ":step1", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== {}.{}",
                            contribution.getStepExecution().getJobExecution().getJobInstance().getJobName(),
                            contribution.getStepExecution().getStepName());
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    @Bean(JOB_NAME + ":step2")
    Step step2() {
        return new StepBuilder(JOB_NAME + ":step2", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== {}.{}",
                            contribution.getStepExecution().getJobExecution().getJobInstance().getJobName(),
                            contribution.getStepExecution().getStepName());
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    @Bean(JOB_NAME + ":step3")
    Step step3() {
        return new StepBuilder(JOB_NAME + ":step3", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== {}.{}",
                            contribution.getStepExecution().getJobExecution().getJobInstance().getJobName(),
                            contribution.getStepExecution().getStepName());
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

}
