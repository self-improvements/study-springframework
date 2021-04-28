package io.github.imsejin.study.springframework.core.aop.model;

import io.github.imsejin.study.springframework.core.aop.annotation.Prefix;
import org.springframework.stereotype.Component;

@Component
public class SimpleReturner implements Returner {

    public static final String PREFIX_1 = "foo and ";
    public static final String PREFIX_2 = "THIS IS PREFIX: ";

    @Override
    @Prefix(PREFIX_1)
    public Object exec(Object arg) {
        return arg;
    }

    @Override
    @Prefix(PREFIX_2)
    public Object exec(Object... args) {
        return args;
    }

}
