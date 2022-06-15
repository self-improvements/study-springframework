package io.github.imsejin.study.springframework.core.databinding.converter;

import io.github.imsejin.study.springframework.core.databinding.formatter.UserFormatter;
import io.github.imsejin.study.springframework.core.databinding.model.Book;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.util.Random;

/**
 * Spring Boot가 아니라면 {@link WebMvcConfigurer#addFormatters(FormatterRegistry)}에서
 * {@link Converter}들을 등록해줘야 한다.
 *
 * <p> {@link ConverterRegistry}에 등록해서 사용한다.
 *
 * <p> {@link org.springframework.core.convert.ConversionService}가 {@link Converter}와 {@link Formatter}들을
 * 사용하여 두 객체 간의 변환을 담당한다. Spring MVC, Spring Bean XML configuration, SpEL 등에서 사용된다.
 *
 * <pre>
 *                           ┌───────────────────┐
 *                           │ ConverterRegistry │
 *                           └─────────▲─────────┘
 *                                     │
 * ┌───────────────────┐     ┌─────────┴─────────┐
 * │ ConversionService │     │ FormatterRegistry │
 * └─────────▲─────────┘     └─────────▲─────────┘
 *           │                         │
 *           └────────────┬────────────┘
 *                        │
 *              ┌─────────┴─────────┐
 *              │ DefaultFormatting │
 *              │ ConversionService │
 *              └───────────────────┘
 * </pre>
 *
 * <p> {@link FormatterRegistry}가 {@link ConverterRegistry}를 상속받고 있기에
 * {@link WebMvcConfigurer}에서 {@link Formatter}와 {@link Converter}를 모두 등록할 수 있다.
 *
 * <p> {@link org.springframework.format.support.DefaultFormattingConversionService}가 두 인터페이스를
 * 구현하여, 변환을 위한 구체적인 로직을 등록하고 실제적인 변환 작업을 가능하게 한다.
 *
 * <p> Spring Boot의 경우 {@link org.springframework.format.support.DefaultFormattingConversionService}를 상속받은
 * {@link org.springframework.boot.autoconfigure.web.format.WebConversionService}을 bean으로 등록되어 사용할 수 있다.
 * {@link Formatter}와 {@link Converter}가 bean으로 등록되어 있으면 자동으로 {@link ConverterRegistry}에 등록해준다.
 */
public class BookConverter {

    private static final Random random = new Random();

    @Component
    public static class StringToBookConverter implements Converter<String, Book> {
        @Override
        public Book convert(String source) {
            return Book.builder()
                    .id(UserFormatter.toId(source))
                    .name(source)
                    .publishedAt(LocalDate.now())
                    .price(random.nextInt(1_000_000))
                    .build();
        }
    }

    @Component
    public static class BookToStringConverter implements Converter<Book, String> {
        @Override
        public String convert(Book source) {
            return source.toString();
        }
    }

}
