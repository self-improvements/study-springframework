package io.github.imsejin.springstudy.validation;

import io.github.imsejin.springstudy.validation.model.Fruit;
import io.github.imsejin.springstudy.validation.validator.FruitValidator;
import io.github.imsejin.springstudy.validation.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class ValidationTest {

    private final Validator customValidator = new FruitValidator();

    @Autowired
    @Qualifier("defaultValidator") // LocalValidatorFactoryBean
    private Validator validator;

    @Test
    void invalidateWithCustomValidator() {
        // given
        Fruit fruit = Fruit.builder()
                .name(" ") // Empty name is not allowed.
                .weight(-100) // Weight must be positive.
                .build();

        // when
        Errors errors = new BeanPropertyBindingResult(fruit, "fruit");
        customValidator.validate(fruit, errors);

        // then
        List<String> errorCodes = errors.getAllErrors().stream()
                .map(ObjectError::getCode).collect(toList());
        assertThat(errorCodes)
                .isNotNull()
                .containsExactlyInAnyOrder(FruitValidator.NAME_ERROR_CODE, FruitValidator.WEIGHT_ERROR_CODE);
    }

    @Test
    void validateWithCustomValidator() {
        // given
        Fruit fruit = Fruit.builder()
                .name("Pineapple")
                .color("Yellow")
                .weight(1500)
                .build();

        // when
        Errors errors = new BeanPropertyBindingResult(fruit, "fruit");
        customValidator.validate(fruit, errors);

        // then
        assertThat(errors.getAllErrors())
                .isNotNull()
                .isEmpty();
    }

    @Test
    void invalidateWithAnnotations() {
        // given
        User user = User.builder()
                .id(-100L) // Positive.
                .name(" ") // Not blank.
                .age(151) // Positive and less than or equal to 150.
                .email("@foo") // Valid email.
                .build();

        Errors errors = new BeanPropertyBindingResult(user, "user");
        this.validator.validate(user, errors);

        List<String> errorCodes = errors.getAllErrors().stream()
                .map(ObjectError::getCode).collect(toList());
        assertThat(errorCodes)
                .isNotNull()
                .containsExactlyInAnyOrder("Positive", "NotBlank", "Max", "Email");
    }

    @Test
    void validateWithAnnotations() {
        // given
        User user = User.builder()
                .id(100_123L)
                .name("Im Sejin")
                .age(150)
                .email("foo@bar.com")
                .build();

        Errors errors = new BeanPropertyBindingResult(user, "user");
        this.validator.validate(user, errors);

        List<String> errorCodes = errors.getAllErrors().stream()
                .map(ObjectError::getCode).collect(toList());
        assertThat(errorCodes)
                .isNotNull()
                .isEmpty();
    }

}
