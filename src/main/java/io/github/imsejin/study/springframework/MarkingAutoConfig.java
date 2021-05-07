package io.github.imsejin.study.springframework;

import io.github.imsejin.study.springframework.annotation.Marking;
import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Set;

@Configuration
public class MarkingAutoConfig {

    @Bean
    Classes markingClasses() {
        Reflections reflections = new Reflections(getClass().getPackageName());
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Marking.class);

        return new Classes(types);
    }

    public static class Classes {
        private final Set<Class<?>> values;

        public Classes(Set<Class<?>> values) {
            this.values = Collections.unmodifiableSet(values);
        }

        public Set<Class<?>> get() {
            return this.values;
        }
    }

}
