package io.github.imsejin.springstudy.profile.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig {

    @Bean
    public Repository<Cloth> clothRepository() {
        return new ClothRepository<>();
    }

}
