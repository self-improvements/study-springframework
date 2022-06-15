package io.github.imsejin.study.springframework.core.config;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

/**
 * Spring framework의 default policy of bean name generator는
 * bean name을 {@link Class#getSimpleName()}로 한다. package가 다르고
 * class name이 같은 경우 {@link BeanDefinitionStoreException}과
 * {@code org.springframework.context.annotation.ConflictingBeanDefinitionException}
 * 예외가 발생한다.
 */
public class FullyQualifiedBeanNameGenerator implements BeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        return definition.getBeanClassName();
    }

}
