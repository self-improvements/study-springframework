package io.github.imsejin.study.springframework.core.validation.validator;

import io.github.imsejin.study.springframework.core.validation.model.Camera;
import io.github.imsejin.study.springframework.core.validation.model.Lens;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor
class AnyOfValidatorTest {

    @Autowired
    private final Validator validator;

    @Nested
    class Success {
        @Test
        void test0() {
//        LocalValidatorFactoryBean validatorFactory = new LocalValidatorFactoryBean();
//        validatorFactory.afterPropertiesSet();
//        Validator validator = validatorFactory;

//        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
//        Validator validator = validatorFactory.getValidator();

            // given
            Lens lens = new Lens();
            lens.setSerialNumber(10018458L);
            lens.setModelName("NIKON-238759");

            Camera camera = new Camera();
            camera.setSerialNumber(12345678L);
            camera.setModelName("");
            camera.setBrandName("AJOL-705461");
            camera.setLens(lens);

            // when
            Set<ConstraintViolation<Camera>> violations = validator.validate(camera);

            // then
            assertThat(violations).isNotNull().isEmpty();
        }
    }

    // -------------------------------------------------------------------------------------------------

    @Nested
    class Failure {
        @Test
        @DisplayName("Camera has invalid modelName and brandName")
        void test0() {
            // given
            Lens lens = new Lens();
            lens.setModelName("NIKON-238759");
            lens.setManufacturedAt(LocalDate.now());

            Camera camera = new Camera();
            camera.setSerialNumber(12345678L);
            camera.setModelName("");
            camera.setBrandName("");
            camera.setLens(lens);

            // when
            Set<ConstraintViolation<Camera>> violations = validator.validate(camera);

            // then
            assertThat(violations)
                    .isNotNull()
                    .isNotEmpty()
                    .allMatch(violation -> violation.getLeafBean() == camera);
        }

        @Test
        @DisplayName("Lens has invalid serialNumber and manufacturedAt")
        void test1() {
            // given
            Lens lens = new Lens();
            lens.setModelName("NIKON-238759");

            Camera camera = new Camera();
            camera.setSerialNumber(12345678L);
            camera.setModelName("AJOL-12345678");
            camera.setBrandName("AJOL-705461");
            camera.setLens(lens);

            // when
            Set<ConstraintViolation<Camera>> violations = validator.validate(camera);

            // then
            assertThat(violations)
                    .isNotNull()
                    .isNotEmpty()
                    .allMatch(violation -> violation.getLeafBean() == lens);
        }

        @Test
        @DisplayName("Lens has invalid modelName and brandName")
        void test2() {
            // given
            Lens lens = new Lens();
            lens.setManufacturedAt(LocalDate.now());

            Camera camera = new Camera();
            camera.setSerialNumber(12345678L);
            camera.setModelName("AJOL-12345678");
            camera.setBrandName("AJOL-705461");
            camera.setLens(lens);

            // when
            Set<ConstraintViolation<Camera>> violations = validator.validate(camera);

            // then
            assertThat(violations)
                    .isNotNull()
                    .isNotEmpty()
                    .allMatch(violation -> violation.getLeafBean() == lens);
        }
    }

}
