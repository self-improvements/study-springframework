package io.github.imsejin.springstudy.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncEventListener {

    /**
     * {@link Async} 애노테이션이 붙으면 {@link Order} 애노테이션은 무의미해진다.
     */
    @Async
    @EventListener
    public void handle1(String event) {
        log.info("[{}] / Listened String Event at handle1: {}", Thread.currentThread().getName(), event);
    }

    @Async
    @EventListener
    public void handle2(String event) {
        log.info("[{}] / Listened String Event at handle2: {}", Thread.currentThread().getName(), event);
    }

}
