package io.github.imsejin.study.springframework.core.validation.validator;

import io.github.imsejin.study.springframework.core.validation.model.Fruit;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class FruitValidator implements Validator {

    public static final String WEIGHT_ERROR_CODE = "positive";

    public static final String NAME_ERROR_CODE = "no-blank";

    @Override
    public boolean supports(Class<?> clazz) {
        return Fruit.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!(target instanceof Fruit)) throw new IllegalArgumentException("Target is not instance of " + Fruit.class);

        // Without utilities.
        Fruit fruit = (Fruit) target;
        if (fruit.getWeight() == null || fruit.getWeight() < 1) {
            errors.rejectValue("weight", WEIGHT_ERROR_CODE, "Weight must be positive");
        }

        // With utilities.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", NAME_ERROR_CODE, "Empty name is not allowed");
    }

}