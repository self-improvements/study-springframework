package io.github.imsejin.springstudy.property;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor
class PropertyTest {

    private final ApplicationContext context;

    @Value("${app.name}")
    private String appName;

    @Value("${who.am.i}")
    private String whoAmI;

    /**
     * {@link org.springframework.context.annotation.PropertySource} 애노테이션을 사용하여
     * 아래의 코드처럼 default {@code application.properties}뿐 아니라 다른 properties도 불러올 수 있다.
     *
     * <pre>{@code
     *     @SpringBootApplication
     *     @PropertySource("classpath:/test.properties")
     *     public class Application {
     *         public static void main(String[] args) {
     *             SpringApplication.run(Application.class, args);
     *         }
     *     }
     * }</pre>
     *
     * <p> {@code JVM System Properties}(e.g. -Dkey="value")로 지정한 property가
     * properties file(application.properties)보다 우선순위가 높다.
     */
    @Test
    void test() {
        // given
        Environment environment = context.getEnvironment();

        // when
        String appName = environment.getProperty("app.name"); // application.properties
        String whoAmI = environment.getProperty("who.am.i"); // test.properties

        // then
        assertThat(appName)
                .isNotNull()
                .isEqualTo(this.appName);
        assertThat(whoAmI)
                .isNotNull()
                .isEqualTo(this.whoAmI);
    }

}
