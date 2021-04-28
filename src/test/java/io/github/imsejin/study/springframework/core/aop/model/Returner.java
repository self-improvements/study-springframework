package io.github.imsejin.study.springframework.core.aop.model;

/**
 * Target interface.
 */
public interface Returner {

    Object exec(Object arg);

    Object exec(Object... args);

}
