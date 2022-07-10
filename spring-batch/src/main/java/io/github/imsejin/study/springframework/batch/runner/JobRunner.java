package io.github.imsejin.study.springframework.batch.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobRunner implements ApplicationRunner {

    private final JobLauncher jobLauncher;

    @Qualifier("simpleJob")
    private final Job job;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var jobParameters = new JobParametersBuilder()
                .addString("name", "user-1")
                .toJobParameters();

        jobLauncher.run(job, jobParameters);
    }

}
