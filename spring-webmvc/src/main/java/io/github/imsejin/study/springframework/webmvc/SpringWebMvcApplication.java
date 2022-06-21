package io.github.imsejin.study.springframework.webmvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Arrays;

/**
 * <dl>
 * <dt>{@link ContextLoaderListener}</dt>
 * <dd>
 * Spring Boot 이전의 servlet programming에서 {@link ContextLoaderListener}를 listener로 web.xml에 등록하면,
 * 각 servlet이 Spring IoC Container({@link WebApplicationContext})에 접근하여 beans를 사용할 수 있게 한다.
 * {@link ServletContext}는 java servlet API, {@link WebApplicationContext}는 spring webmvc API다.
 * 둘은 별개의 관계다.
 *
 * <ol>
 * <li>servlet listener method {@link ServletContextListener#contextInitialized(ServletContextEvent)}를 호출</li>
 * <li>{@link ServletContext#setAttribute(String, Object)}를 이용하여 {@link WebApplicationContext}를 저장</li>
 * <li>{@link ServletContext#getAttribute(String)}를 이용하여 {@link WebApplicationContext}를 볼러옴</li>
 * <li>{@link WebApplicationContext#getBean(Class)}을 이용하여 spring beans를 이용할 수 있음</li>
 * </ol>
 * </dd>
 *
 * <dt>{@link DispatcherServlet}</dt>
 * <dd>
 * Front controller(모든 requests를 하나의 servlet이 받아 공통 로직을 처리하고 세부 로직은 다른 servlet이 실행하도록
 * dispatch하는 servlet, 또는 그러한 design pattern)로서 web과 관련없는 beans를 담은
 * Root WebApplicationContext<sup>전자</sup>를 상속하여 Servlet WebApplicationContext<sup>후자</sup>를 만든다.
 * 전자는 global scope으로 다른 DispatcherServlet에서 특정 bean이나 attribute를 공유할 수 있고,
 * 후자는 local scope으로 다른 DispatcherServlet에서 이에 접근할 수 없다.
 *
 * <pre>
 * ┌───────── DispatcherServlet ─────────┐
 * │ ┌─ Servlet WebApplicationContext ─┐ │
 * │ │ - Controllers                   │ │
 * │ │ - HandlerMappings               │ │
 * │ │ - MultipartResolver             │ │
 * │ │ - ViewResolvers                 │ │
 * │ │ - and other web-related beans   │ │
 * │ └──┬──────────────────────────────┘ │
 * │    │ Delegates if no bean found     │
 * │    ▼                                │
 * │ ┌─── Root WebApplicationContext ──┐ │
 * │ │ - Services                      │ │
 * │ │ - Repositories                  │ │
 * │ │ - and etc.                      │ │
 * │ └─────────────────────────────────┘ │
 * └─────────────────────────────────────┘
 * </pre>
 * </dd>
 * </dl>
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "io.github.imsejin.study.springframework")
public class SpringWebMvcApplication implements ApplicationRunner {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringWebMvcApplication.class, args);

//        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        StandardReflectionParameterNameDiscoverer discoverer = new StandardReflectionParameterNameDiscoverer();
//        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        Constructor<PublicFinalArgs> constructor = PublicFinalArgs.class.getDeclaredConstructor(Integer.class, String.class, URL.class);
        String[] parameterNames = discoverer.getParameterNames(constructor);
        System.out.println(Arrays.toString(parameterNames));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
    }

    @RequiredArgsConstructor
    public static class PublicFinalArgs {
        private final Integer id;
        private final String title;
        private final URL url;
    }

}
