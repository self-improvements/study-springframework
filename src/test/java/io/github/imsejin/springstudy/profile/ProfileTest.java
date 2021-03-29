package io.github.imsejin.springstudy.profile;

import io.github.imsejin.springstudy.profile.model.Cloth;
import io.github.imsejin.springstudy.profile.model.Repository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor
// Must be specified as 'test'.
@ActiveProfiles("test")
class ProfileTest {

    private final ApplicationContext context;

    @Autowired
    private Repository<Cloth> clothRepository;

    @Test
    void profile() {
        // given
        Environment environment = context.getEnvironment();
        String[] activeProfiles = environment.getActiveProfiles();
        String[] defaultProfiles = environment.getDefaultProfiles();

        // when & then
        assertThat(activeProfiles).anyMatch("test"::equals);
        assertThat(defaultProfiles).anyMatch("default"::equals);

        // then
        assertThat(clothRepository).isNotNull();
        Cloth cloth = new Cloth();
        assertThat(clothRepository.save(cloth)).isEqualTo(cloth);
    }

}
