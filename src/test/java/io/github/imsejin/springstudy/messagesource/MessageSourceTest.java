package io.github.imsejin.springstudy.messagesource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MessageSourceTest {

    @Autowired
    private MessageSource messageSource;

    @Test
    void messageSource() {
        // given
        String messageCode = "greetings";
        Object[] arguments = {"World!"};

        // when
        String greetingsEnglish = messageSource.getMessage(messageCode, arguments, Locale.US); // messages_en_US.properties
        String greetingsKorean = messageSource.getMessage(messageCode, arguments, Locale.KOREA); // messages_ko_KR.properties

        // then
        assertThat(greetingsEnglish).isEqualTo("Hello, World!");
        assertThat(greetingsKorean).isEqualTo("안녕, World!");
    }

}
