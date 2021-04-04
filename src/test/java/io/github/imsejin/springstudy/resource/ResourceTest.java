package io.github.imsejin.springstudy.resource;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.context.support.ServletContextResource;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class ResourceTest {

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * ClassPath 또는 ServletContext를 기준으로 resource를 읽어오는 API가 없다.
     * {@link Resource}는 {@link java.net.URL}을 추상화한 위의 needs를 해결했다.
     *
     * <p> {@link Resource}의 여러 구현체 중 일부만 살펴보면,
     * <dl>
     *     <dt>{@link UrlResource}</dt>
     *     <dd>기본적으로 지원하는 protocol: http, https, ftp, file, jar</dd>
     *
     *     <dt>{@link ClassPathResource}</dt>
     *     <dd>classpath</dd>
     *
     *     <dt>{@link FileSystemResource}</dt>
     *     <dd>file system을 이용</dd>
     *
     *     <dt>{@link ServletContextResource}</dt>
     *     <dd>web application root에서 상대경로로 resource를 찾음</dd>
     * </dl>
     *
     * <p> 위의 어떤 {@link Resource}의 구현체를 사용하느냐를 결정하는 건
     * location 문자열과 {@link ApplicationContext}의 타입에 따라 결정된다.
     * {@link ApplicationContext}가 인터페이스이기에 어떤 구현체가
     * {@link ApplicationContext#getResource(String)}를
     * 호출하느냐에 따라 반환되는 {@link Resource}의 구현체가 달라진다.
     *
     * <dl>
     *     <dt>{@link GenericWebApplicationContext}</dt>
     *     <dd>기본적으로 지원하는 protocol: http, https, ftp, file, jar</dd>
     *
     *     <dt>{@link ClassPathXmlApplicationContext}</dt>
     *     <dd>classpath</dd>
     *
     *     <dt>{@link FileSystemXmlApplicationContext}</dt>
     *     <dd>
     *         file system을 이용(무조건 상대경로로 resource를 찾음)
     *         (절대경로를 이용하고 싶으면 "file://"이라는 prefix를 사용)
     *     </dd>
     *
     *     <dt>{@link WebApplicationContext}</dt>
     *     <dd>web application root에서 상대경로로 resource를 찾음</dd>
     * </dl>
     *
     * <p> {@link ApplicationContext}의 타입에 상관없이 일관된 {@link Resource} 타입을
     * 유지하려면 {@link URL}의 prefix(+ "classpath:") 중 하나를 사용할 수 있다.
     */
    @Test
    void test() {
        // given
        var ac1 = new GenericWebApplicationContext();
        var ac2 = new ClassPathXmlApplicationContext();
        var ac3 = new FileSystemXmlApplicationContext();
        var ac4 = new ServletWebServerApplicationContext();

        // when
        Resource r1 = ac1.getResource("https://jsonplaceholder.typicode.com/posts");
        Resource r2 = ac2.getResource("application.properties");
        Resource r3 = ac3.getResource("application.properties");
        Resource r4 = ac4.getResource("application.properties");
        Resource r5 = ac4.getResource("https://jsonplaceholder.typicode.com/posts");

        // then: 1
        assertThat(r1.getClass())
                .as("Type of resource loaded by 'GenericWebApplicationContext' is 'UrlResource'")
                .isEqualTo(UrlResource.class);
        assertThat(r2.getClass())
                .as("Type of resource loaded by 'FileSystemXmlApplicationContext' is 'DefaultResourceLoader.ClassPathContextResource'")
                .hasSuperclass(ClassPathResource.class);
        assertThat(r3.getClass())
                .as("Type of resource loaded by 'FileSystemXmlApplicationContext' is 'FileSystemResource'")
                .isEqualTo(FileSystemResource.class);
        assertThat(r4.getClass())
                .as("Type of resource loaded by 'ServletWebServerApplicationContext' is 'DefaultResourceLoader.ClassPathContextResource'")
                .hasSuperclass(ClassPathResource.class);
        assertThat(r5.getClass())
                .as("Type of resource loaded by 'ServletWebServerApplicationContext' is 'UrlResource'")
                .isEqualTo(UrlResource.class);

        // then: 2
        assertThat(resourceLoader.getResource("https://application.properties").getClass())
                .isEqualTo(UrlResource.class);
        assertThat(resourceLoader.getResource("ftp://application.properties").getClass())
                .isEqualTo(UrlResource.class);
        assertThat(resourceLoader.getResource("file:///application.properties").getClass())
                .isEqualTo(FileUrlResource.class);
        assertThat(resourceLoader.getResource("classpath:application.properties").getClass())
                .isEqualTo(ClassPathResource.class);
        assertThat(resourceLoader.getResource("jar:application.properties").getClass())
                .isEqualTo(ServletContextResource.class);
    }

}
