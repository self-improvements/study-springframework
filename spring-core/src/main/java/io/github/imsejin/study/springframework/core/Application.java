package io.github.imsejin.study.springframework.core;

import io.github.imsejin.study.springframework.core.config.FullyQualifiedBeanNameGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@EnableAsync
@SpringBootApplication(
        nameGenerator = FullyQualifiedBeanNameGenerator.class,
        scanBasePackages = "io.github.imsejin.study.springframework"
)
@PropertySource("classpath:/test.properties")
@RequiredArgsConstructor
public class Application implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

}
