package io.github.imsejin.springstudy.event.model.listener;

import io.github.imsejin.springstudy.event.model.object.OldMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.EventListener;
import java.util.EventObject;

/**
 * {@link EventObject}를 전달할 대상을 알기 위해,
 * {@link EventListener}는 반드시 "bean"으로 등록되어야 한다.
 *
 * <p> Spring framework 4.2전까지 {@link ApplicationListener}를 구현해야 했다.
 */
@Slf4j
@Component
public class OldMessageEventListener implements ApplicationListener<OldMessageEvent> {

    @Override
    public void onApplicationEvent(OldMessageEvent event) {
        log.info("[{}] / Listened OldMessageEvent: {}", Thread.currentThread().getName(), event.getMessage());
    }

}
