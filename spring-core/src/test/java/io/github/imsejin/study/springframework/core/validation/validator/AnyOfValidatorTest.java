package io.github.imsejin.study.springframework.core.validation.validator;

import io.github.imsejin.study.springframework.core.validation.model.Camera;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor
class AnyOfValidatorTest {

    @Autowired
    private final Validator validator;

    @Test
    void success() {
//        LocalValidatorFactoryBean validatorFactory = new LocalValidatorFactoryBean();
//        validatorFactory.afterPropertiesSet();
//        Validator validator = validatorFactory;

//        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
//        Validator validator = validatorFactory.getValidator();

        // given
        Camera camera = new Camera();
        camera.setSerialNumber(12345678L);
        camera.setModelName("AJOL-12345678");
        camera.setBrandName("AJOL-705461");

        // when
        Set<ConstraintViolation<Camera>> constraintViolations = validator.validate(camera);

        // then
        System.out.println(constraintViolations);
        assertThat(constraintViolations).isNotNull().isEmpty();
    }

}
