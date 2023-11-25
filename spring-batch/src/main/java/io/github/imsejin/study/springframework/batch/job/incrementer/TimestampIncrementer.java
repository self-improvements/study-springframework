package io.github.imsejin.study.springframework.batch.job.incrementer;

import java.time.Instant;
import java.util.Objects;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

/**
 * @see RunIdIncrementer
 */
public class TimestampIncrementer implements JobParametersIncrementer {

    private static final String KEY = "timestamp";

    @Override
    public JobParameters getNext(JobParameters parameters) {
        JobParameters jobParameters = Objects.requireNonNullElseGet(parameters, JobParameters::new);
        long value = Instant.now().toEpochMilli();

        return new JobParametersBuilder(jobParameters)
                .addLong(KEY, value)
                .toJobParameters();
    }

}
