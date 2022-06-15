package io.github.imsejin.study.springframework.core.aop;

import io.github.imsejin.study.springframework.core.aop.model.Returner;
import io.github.imsejin.study.springframework.core.aop.model.SimpleReturner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AopTest {

    @Autowired
    private Returner returner;

    @Test
    void test() {
        // given
        String arg = "bar";
        Object[] args = {"Hello", "World", "!"};

        // when
        Object bar = returner.exec(arg);
        Object[] result = (Object[]) returner.exec(args);

        // then
        assertThat(bar)
                .isNotEqualTo(arg)
                .isEqualTo(SimpleReturner.PREFIX_1 + arg);
        assertThat(result)
                .doesNotContain(arg)
                .containsExactly(Stream.of("Hello", "World", "!")
                        .map(it -> SimpleReturner.PREFIX_2 + it).toArray());
    }

}
