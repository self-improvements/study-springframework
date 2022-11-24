package io.github.imsejin.study.springframework.batch.job;

import io.github.imsejin.study.springframework.batch.job.validator.NotEmptyJobParametersValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class ValidatedJobConfig {

    public static final String JOB_NAME = "validatedJob";

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean(JOB_NAME)
    Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step1())
                .next(step2())
                .validator(new NotEmptyJobParametersValidator())
//                .validator(new DefaultJobParametersValidator(new String[] {"version"}, new String[] {"sequence"}))
                .build();
    }

    @Bean(JOB_NAME + "step1")
    Step step1() {
        return stepBuilderFactory.get(JOB_NAME + "step1")
                .tasklet(((contribution, chunkContext) -> {
                    log.info("===== Step 1: {}, {}", contribution, chunkContext);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean(JOB_NAME + "step2")
    Step step2() {
        return stepBuilderFactory.get(JOB_NAME + "step2")
                .tasklet(((contribution, chunkContext) -> {
                    log.info("===== Step 2: {}, {}", contribution, chunkContext);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

}
