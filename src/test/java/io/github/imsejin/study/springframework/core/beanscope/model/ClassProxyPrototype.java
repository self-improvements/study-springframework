package io.github.imsejin.study.springframework.core.beanscope.model;

import lombok.Getter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Getter
@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ClassProxyPrototype {

    private final UUID uuid = UUID.randomUUID();

    public final UUID uuid2 = UUID.randomUUID();

}
