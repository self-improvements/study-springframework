package io.github.imsejin.study.springframework.webmvc;

import io.github.imsejin.study.springframework.MarkingAutoConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages = "io.github.imsejin.study.springframework")
@RequiredArgsConstructor
public class Application implements ApplicationRunner {

    private final MarkingAutoConfig.Classes classes;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        classes.get().forEach(it -> log.info("{}", it));
    }

}
