package io.github.imsejin.study.springframework.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
class FlowJob1Config {

    static final String JOB_NAME = "flowJob1";

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean(JOB_NAME)
    Job job() {
        return jobBuilderFactory.get(JOB_NAME)
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
        return stepBuilderFactory.get(JOB_NAME + ":step1")
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== {}.{}", contribution.getStepExecution().getJobExecution().getJobInstance().getJobName(),
                            contribution.getStepExecution().getStepName());
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean(JOB_NAME + ":step2")
    Step step2() {
        return stepBuilderFactory.get(JOB_NAME + ":step2")
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== {}.{}", contribution.getStepExecution().getJobExecution().getJobInstance().getJobName(),
                            contribution.getStepExecution().getStepName());
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean(JOB_NAME + ":step3")
    Step step3() {
        return stepBuilderFactory.get(JOB_NAME + ":step3")
                .tasklet(((contribution, chunkContext) -> {
                    log.debug("===== {}.{}", contribution.getStepExecution().getJobExecution().getJobInstance().getJobName(),
                            contribution.getStepExecution().getStepName());
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

}
