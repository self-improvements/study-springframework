package io.github.imsejin.study.springframework.core.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.RequestHandledEvent;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Spring framework가 제공하는 기본 Event가 있다.
 *
 * <dl>
 *     <dt>{@link ContextRefreshedEvent}</dt>
 *     <dd>{@link ApplicationContext}를 init()하거나 refresh()한 시점에 발생.</dd>
 *
 *     <dt>{@link ContextStartedEvent}</dt>
 *     <dd>{@link ApplicationContext}를 start()하여 lifecycle beans가 시작 신호를 받은 시점에 발생.</dd>
 *
 *     <dt>{@link ContextStoppedEvent}</dt>
 *     <dd>{@link ApplicationContext}를 stop()하여 lifecycle beans가 정자 신호를 받은 시점에 발생.</dd>
 *
 *     <dt>{@link ContextClosedEvent}</dt>
 *     <dd>{@link ApplicationContext}를 close()하여 singleton beans가 소멸하는 시점에 발생.</dd>
 *
 *     <dt>{@link RequestHandledEvent}</dt>
 *     <dd>HTTP 요청을 처리했을 때에 발생.</dd>
 * </dl>
 */
@Slf4j
@Component
public class ApplicationContextEventListener {

    @Autowired
    private ApplicationContext context;

    @EventListener
    public void handle(ContextRefreshedEvent event) {
        log.info("[{}] / Listened ContextRefreshedEvent: {}", Thread.currentThread().getName(), event.getSource());

        assertThat(event.getApplicationContext())
                .isNotNull()
                .isEqualTo(context);
        assertThat(event.getApplicationContext().getBean(getClass()))
                .isNotNull()
                .isEqualTo(this);
    }

    @EventListener
    public void handle(ContextClosedEvent event) {
        log.info("[{}] / Listened ContextClosedEvent: {}", Thread.currentThread().getName(), event.getSource());

        assertThat(event.getApplicationContext())
                .isNotNull()
                .isEqualTo(context);
        assertThat(event.getApplicationContext().getBean(getClass()))
                .isNotNull()
                .isEqualTo(this);
    }

}
