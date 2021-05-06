package io.github.imsejin.study.springframework;

import io.github.imsejin.study.springframework.annotation.Marking;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class MarkingAutoConfig {

    @Bean
    Classes markingClasses() {
        Reflections reflections = new Reflections("io.github.imsejin.study.springframework");
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Marking.class);

        return new Classes(types);
    }

    @RequiredArgsConstructor
    public static class Classes {
        private final Set<Class<?>> values;

        public Set<Class<?>> get() {
            return this.values;
        }
    }

}
