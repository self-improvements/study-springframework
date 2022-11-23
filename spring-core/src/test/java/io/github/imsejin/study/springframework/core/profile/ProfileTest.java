package io.github.imsejin.study.springframework.core.profile;

import io.github.imsejin.study.springframework.core.profile.model.Cloth;
import io.github.imsejin.study.springframework.core.profile.model.Repository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor
@ActiveProfiles("test")
class ProfileTest {

    private final ApplicationContext context;

    // This can be loaded when active profile is 'test'.
    @Autowired
    private Repository<Cloth> clothRepository;

    @Test
    void profile() {
        // given
        String[] activeProfiles = context.getEnvironment().getActiveProfiles();

        // when & then
        assertThat(activeProfiles).containsOnly("test");

        // then
        Cloth cloth = new Cloth();
        assertThat(clothRepository)
                .isNotNull()
                .returns(cloth, it -> it.save(cloth));
    }

}
