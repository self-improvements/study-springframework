package io.github.imsejin.springstudy.event.model;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Spring framework 4.2전까지 {@link ApplicationEvent}를 상속받아야 한다.
 */
@Getter
public class OldMessageEvent extends ApplicationEvent {

    private final String message;

    public OldMessageEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

}
