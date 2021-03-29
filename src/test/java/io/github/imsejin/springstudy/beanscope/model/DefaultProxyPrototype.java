package io.github.imsejin.springstudy.beanscope.model;

import lombok.Getter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Getter
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DefaultProxyPrototype {

    private final UUID uuid = UUID.randomUUID();

    public final UUID uuid2 = UUID.randomUUID();

}
