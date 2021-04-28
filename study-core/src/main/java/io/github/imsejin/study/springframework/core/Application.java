package io.github.imsejin.study.springframework.core;

import io.github.imsejin.study.springframework.core.config.FullyQualifiedBeanNameGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(nameGenerator = FullyQualifiedBeanNameGenerator.class)
@PropertySource("classpath:/test.properties")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
