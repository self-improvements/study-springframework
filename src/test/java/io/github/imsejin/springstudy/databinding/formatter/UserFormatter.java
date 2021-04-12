package io.github.imsejin.springstudy.databinding.formatter;

import io.github.imsejin.springstudy.validation.model.User;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditor;
import java.text.ParseException;
import java.util.Locale;

/**
 * {@link PropertyEditor}와는 달리 {@link Formatter}는 thread-safe하다.
 * 따라서 bean으로 등록해도 안전하다.
 *
 * <p> {@link PropertyEditor}의 대체제이며 {@link Object}와 {@link String} 간의 변환을 담당한다.
 * 문자열을 {@link Locale}에 따라 internationalization 하는 기능을 제공한다.
 *
 * <p> {@link org.springframework.format.FormatterRegistry}에 등록해서 사용한다.
 */
@Component
public class UserFormatter implements Formatter<User> {

    public static long toId(String name) {
        return name.chars().sum();
    }

    @Override
    public User parse(String text, Locale locale) throws ParseException {
        return User.builder()
                .id(toId(text))
                .name(text)
                .build();
    }

    @Override
    public String print(User object, Locale locale) {
        return object.toString();
    }

}
