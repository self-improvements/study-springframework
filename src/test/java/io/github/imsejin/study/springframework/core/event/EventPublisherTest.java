package io.github.imsejin.study.springframework.core.event;

import io.github.imsejin.study.springframework.core.event.model.NewMessageEvent;
import io.github.imsejin.study.springframework.core.event.model.OldMessageEvent;
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
        log.info("========== Published String Event!");
        publisher.publishEvent("Hello Stringified World!");

        log.info("========== Published OldMessageEvent!");
        publisher.publishEvent(new OldMessageEvent(this, "Hello Old World!"));

        log.info("========== Published NewMessageEvent!");
        publisher.publishEvent(new NewMessageEvent(this, "Hello New World!"));

        log.info("========== Finished publishing events!");
    }

}
