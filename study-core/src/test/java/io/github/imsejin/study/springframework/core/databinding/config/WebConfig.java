package io.github.imsejin.study.springframework.core.databinding.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Spring Boot에서는 더 이상 이렇게 {@link Converter}나 {@link Formatter}를
     * 일일이 등록할 필요가 없다. bean으로 등록되면 자동으로 사용된다.
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new BookConverter.BookToStringConverter());
//        registry.addConverter(new BookConverter.StringToBookConverter());
//        registry.addFormatter(new UserFormatter());
    }

}
