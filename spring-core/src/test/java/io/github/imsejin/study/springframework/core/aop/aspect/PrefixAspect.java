package io.github.imsejin.study.springframework.core.aop.aspect;

import io.github.imsejin.study.springframework.core.aop.annotation.Prefix;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @see Prefix#value()
 */
@Aspect
@Component
public class PrefixAspect {

    @Pointcut("@annotation(io.github.imsejin.study.springframework.core.aop.annotation.Prefix)")
    private void prefixAnnotation() {
    }

    @Around("prefixAnnotation()")
    public Object execute(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String prefix = method.getAnnotation(Prefix.class).value();


        Object[] args = pjp.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String) {
                args[i] = prefix + args[i];

            } else if (args[i].getClass().isArray()) {
                Object[] arr = (Object[]) args[i];

                for (int j = 0; j < arr.length; j++) {
                    if (arr[j] instanceof String) arr[j] = prefix + arr[j];
                }
            }
        }

        // 파라미터를 조작할 수 있다.
        return pjp.proceed(args);
    }

}
