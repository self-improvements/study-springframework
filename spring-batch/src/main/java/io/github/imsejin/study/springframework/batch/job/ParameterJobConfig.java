package io.github.imsejin.study.springframework.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JobParameter vs 시스템 변수
 *
 * <p> 일단 첫번째로, 시스템 변수를 사용할 경우 Spring Batch의 Job Parameter 관련 기능을 못쓰게 됩니다.
 * 예를 들어, Spring Batch는 같은 JobParameter로 같은 Job을 두 번 실행하지 않습니다.
 * 하지만 시스템 변수를 사용하게 될 경우 이 기능이 전혀 작동하지 않습니다.
 * 또한 Spring Batch에서 자동으로 관리해주는 Parameter 관련 메타 테이블이 전혀 관리되지 않습니다.
 * Job Parameter를 못쓴다는 것은 Late Binding을 못하게 된다는 의미입니다.
 *
 * <p> 둘째, Command line이 아닌 다른 방법으로 Job을 실행하기가 어렵습니다.
 * 만약 실행해야한다면 전역 상태 (시스템 변수 혹은 환경 변수)를 동적으로 계속해서 변경시킬 수 있도록 Spring Batch를 구성해야합니다.
 * 동시에 여러 Job을 실행하려는 경우 또는 테스트 코드로 Job을 실행해야할때 문제가 발생할 수 있습니다.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ParameterJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    Job parameterJob() {
        return jobBuilderFactory.get("parameterJob")
                .start(parameterStep0(null))
                .next(parameterStep1(null))
                .build();
    }

    @Bean
    // To use job parameters, annotate @JobScope.
    @JobScope
    Step parameterStep0(@Value("#{jobParameters['param']}") String param) {
        return stepBuilderFactory.get("parameterStep0")
                .tasklet(((contribution, chunkContext) -> {
                    log.info("===== Step 1: {}", param);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    // To use job parameters, annotate @JobScope.
    @JobScope
    Step parameterStep1(@Value("#{jobParameters['param']}") String param) {
        return stepBuilderFactory.get("parameterStep1")
                .tasklet(((contribution, chunkContext) -> {
                    log.info("===== Step 2: {}", param);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

}
