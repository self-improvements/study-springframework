package io.github.imsejin.springstudy.event.listener;

import io.github.imsejin.springstudy.event.model.NewMessageEvent;
import io.github.imsejin.springstudy.event.model.OldMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.EventObject;

/**
 * {@link EventObject}를 전달할 대상을 알기 위해,
 * {@link java.util.EventListener}는 반드시 "bean"으로 등록되어야 한다.
 *
 * <p> Spring framework 4.2부터 {@link ApplicationListener}를 구현하지 않아도 된다.
 *
 * <p> 기본적으로 각 EventHandler는 synchronized로 처리된다.
 */
@Slf4j
@Component
public class NewMessageEventListener {

    @EventListener
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void handle(OldMessageEvent event) {
        log.info("[{}] / Listened OldMessageEvent: {}", Thread.currentThread().getName(), event.getMessage());
    }

    @EventListener
    public void handle3(NewMessageEvent event) {
        log.info("[{}] / Listened NewMessageEvent at handle3: {}", Thread.currentThread().getName(), event.getMessage());
    }

    @EventListener
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public void handle2(NewMessageEvent event) {
        log.info("[{}] / Listened NewMessageEvent at handle2: {}", Thread.currentThread().getName(), event.getMessage());
    }

    @EventListener
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void handle1(NewMessageEvent event) {
        log.info("[{}] / Listened NewMessageEvent at handle1: {}", Thread.currentThread().getName(), event.getMessage());
    }

}
