package io.github.imsejin.springstudy.event;

import io.github.imsejin.springstudy.event.model.object.NewMessageEvent;
import io.github.imsejin.springstudy.event.model.object.OldMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j

@SpringBootTest
class EventPublisherTest {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Test
    void test() {
        log.info("Published event!");

        publisher.publishEvent(new OldMessageEvent(this, "Hello Old World!"));
        publisher.publishEvent(new NewMessageEvent(this, "Hello New World!"));
        publisher.publishEvent("Hello Stringified World!");
    }

}
