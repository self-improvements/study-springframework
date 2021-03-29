package io.github.imsejin.springstudy.beanscope;

import io.github.imsejin.springstudy.beanscope.model.ClassProxyPrototype;
import io.github.imsejin.springstudy.beanscope.model.DefaultProxyPrototype;
import io.github.imsejin.springstudy.beanscope.model.Singleton;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor
class BeanScopeTest {

    @Autowired
    private Singleton singleton;

    @Autowired
    private DefaultProxyPrototype defaultProxyPrototype;

    @Autowired
    private ClassProxyPrototype classProxyPrototype;

    private final ApplicationContext context;

    /**
     * Spring bean의 default scope은 {@link BeanDefinition#SCOPE_SINGLETON}이다.
     * {@link Component} 애노테이션을 붙여 spring bean으로 등록한 객체를 IOC 컨테이너가 관리한다.
     *
     * <p> IOC 컨테이너가 spring application 구동 시 {@link Singleton#Singleton(DefaultProxyPrototype, ClassProxyPrototype)}
     * 생성자를 사용하여 {@link Singleton}의 instance를 오직 1개만 생성한다.
     *
     * <p> {@link BeanDefinition#SCOPE_PROTOTYPE}을 갖는 spring bean의 경우,
     * 모든 의존관계(dependency injection point)와 {@link BeanFactory}에서 직접 bean을
     * 가져와 사용할 때마다 {@link DefaultProxyPrototype} 생성자를 사용하여
     * {@link DefaultProxyPrototype}의 instance를 매번 생성한다. 각각의 {@link DefaultProxyPrototype}은 다른 객체다.
     *
     * <p> {@link Singleton} 내 {@link ClassProxyPrototype} 필드처럼,
     * singleton bean이 prototype bean을 필드로 가질 때 문제가 복잡해진다.
     * {@link Scope#scopeName()}을 {@link BeanDefinition#SCOPE_PROTOTYPE}로 지정해도, 이를 갖고 있는 부모 bean이
     * singleton이기에 {@link Singleton#getClassProxyPrototype()}으로 반환되는 instance는 오직 1개다.
     *
     * <p> 이를 해결하려면 {@link Scope#proxyMode()}에 {@link ScopedProxyMode#TARGET_CLASS}를 지정해줘야 한다.
     * {@code ClassProxyPrototype$$EnhancerBySpringCGLIB}처럼 {@link ClassProxyPrototype}를 상속받은 proxy를 오직 1개만
     * 생성하여(parent class가 singleton이기에, proxy도 singleton으로 생성) 의존관계에 주입된다.
     * {@link ClassProxyPrototype}의 instance의 메서드를 호출할 때마다 Spring CGLib가
     * {@link ClassProxyPrototype} 생성자로 target class의 instance를 매번 생성하여 그 operation을 수행한다.
     * 실제 proxy instance는 singleton이지만 매번 target class의 instance를 생성하니,
     * 마치 prototype인 것처럼 동작하도록 구현되어 있다.
     * 따라서 {@link BeanFactory}에서 꺼낼 때마다 다른 객체인 건 물론이고,
     * 아래의 코드처럼 심지어 proxy를 다른 변수에 저장해도 매번 다른 결과를 반환한다.
     *
     * <pre>{@code
     *     ClassProxyPrototype prototype = this.singleton.getClassProxyPrototype();
     *     prototype.getUuid(); // a69571e5-6931-4303-b0fc-a2012815183a
     *     prototype.getUuid(); // abab093a-45b0-4e1f-9024-5276182a90c9
     * }</pre>
     *
     * <p> 이처럼 사용에 매우 주의해야 한다. CGLib로 생성한 클래스의 proxy가 메서드를 실행할 때,
     * {@link MethodInterceptor#intercept(Object, Method, Object[], MethodProxy)}로 원래의 메서드를 대신 실행한다.
     * {@code org.springframework.aop.framework.CglibAopProxy.DynamicAdvisedInterceptor}가 이를 구현했다.
     * 이외에 특정 메서드를 담당하는 interceptor가 있는데 아래와 같다.
     *
     * <dl>
     *     <dt>{@link #equals(Object)}</dt>
     *     <dd>{@code org.springframework.aop.framework.CglibAopProxy.EqualsInterceptor}</dd>
     *     <dt>{@link #hashCode()}</dt>
     *     <dd>{@code org.springframework.aop.framework.CglibAopProxy.HashCodeInterceptor}</dd>
     * </dl>
     *
     * <p> {@code prototype.equals(prototype)} 이런 코드를 {@code DynamicAdvisedInterceptor}가 담당하면
     * 매번 다른 instance가 생성되어 무조건 {@code false}를 반환할 것이기에 저 위의 2개 메서드만을 위한
     * {@code EqualsInterceptor}, {@code HashCodeInterceptor}가 존재한다.
     * 따라서 이 메서드들을 호출할 때는 target class의 instance를 생성하지 않는다.
     */
    @Test
    @DisplayName("Bean의 Scope 설정")
    void beanScopes() {
        // Singleton.
        assertThat(this.singleton)
                .as("Singleton")
                .isEqualTo(context.getBean(Singleton.class));

        // Default Proxy Prototype.
        assertThat(this.singleton.getDefaultProxyPrototype())
                .as("'DefaultProxyPrototype' in singleton bean is always equal to 'DefaultProxyPrototype' in other singleton bean.")
                .isEqualTo(context.getBean(Singleton.class).getDefaultProxyPrototype())
                .as("'DefaultProxyPrototype' in bean differs from 'DefaultProxyPrototype' in other bean.")
                .isNotEqualTo(this.defaultProxyPrototype)
                .as("'DefaultProxyPrototype' in singleton bean differs from 'DefaultProxyPrototype' in 'ApplicationContext'.")
                .isNotEqualTo(context.getBean(DefaultProxyPrototype.class));

        // Field of Default Proxy Prototype.
        assertThat(this.singleton.getDefaultProxyPrototype().uuid2)
                .isNotNull()
                .isEqualTo(context.getBean(Singleton.class).getDefaultProxyPrototype().uuid2)
                .isNotEqualTo(this.defaultProxyPrototype.uuid2)
                .isNotEqualTo(context.getBean(DefaultProxyPrototype.class).uuid2);

        // Class Proxy Prototype: not instantiate target class.
        assertThat(this.singleton.getClassProxyPrototype())
                .as("'ClassProxyPrototype' in singleton bean is always equal to 'ClassProxyPrototype' in other singleton bean.")
                .isEqualTo(this.classProxyPrototype)
                .as("'ClassProxyPrototype' in singleton bean is always equal to 'ClassProxyPrototype' in 'ApplicationContext'.")
                .isEqualTo(context.getBean(ClassProxyPrototype.class));

        // Field of Class Proxy Prototype: instantiates target class 4 times.
        assertThat(this.singleton.getClassProxyPrototype().getUuid())
                .isNotNull()
                .isNotEqualTo(this.classProxyPrototype.getUuid())
                .isNotEqualTo(this.singleton.getClassProxyPrototype().getUuid())
                .isNotEqualTo(context.getBean(ClassProxyPrototype.class).getUuid());

        // It doesn't work if directly accesses field.
        assertThat(this.singleton.getClassProxyPrototype().uuid2).isNull();
        assertThat(this.classProxyPrototype.uuid2).isNull();
        assertThat(context.getBean(ClassProxyPrototype.class).uuid2).isNull();
    }

}
