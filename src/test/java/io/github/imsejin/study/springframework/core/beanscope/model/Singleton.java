package io.github.imsejin.study.springframework.core.beanscope.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class Singleton {

    private final DefaultProxyPrototype defaultProxyPrototype;

    private final ClassProxyPrototype classProxyPrototype;

}
