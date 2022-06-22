package io.github.imsejin.study.springframework.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @see <a href="https://docs.spring.io/spring-batch/docs/current/reference/html/schema-appendix.html">
 * Spring Batch Meta-Data Schema</a>
 */
@EnableBatchProcessing
@SpringBootApplication
public class SpringBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
    }

}
