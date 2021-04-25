package io.github.imsejin.study.springframework.core.resourceloader;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class ResourceLoaderTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    void exists() {
        // given
        String location = "classpath:messages_ko_KR.properties";

        // when
        Resource resource = resourceLoader.getResource(location);

        // then
        assertThat(resource)
                .isNotNull()
                .returns(true, Resource::exists);
    }

    @Test
    void getContent() throws IOException {
        // given
        String location = "classpath:application.properties";

        // when
        Resource resource = resourceLoader.getResource(location);

        // then
        assertThat(resource)
                .isNotNull()
                .returns(true, Resource::exists);
        assertThat(Files.readString(Path.of(resource.getURI())))
                .startsWith("app.name=");
    }

}
