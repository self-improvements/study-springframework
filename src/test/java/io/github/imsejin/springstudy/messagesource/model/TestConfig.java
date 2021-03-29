package io.github.imsejin.springstudy.messagesource.model;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

@Configuration
public class TestConfig {

    /**
     * {@link ReloadableResourceBundleMessageSource}은 {@code messages.properties}가 변경된 걸 감지하여
     * 새로운 message를 로드한다.
     *
     * @return {@link MessageSource}
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
        messageSource.setCacheMillis(5L);

        return messageSource;
    }

}
