package io.github.imsejin.study.springframework.core.validation.validator;

import io.github.imsejin.study.springframework.core.validation.constraint.AnyOf;
import jakarta.validation.*;
import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.ConstraintDescriptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.validation.ValidationUtils;

import java.lang.annotation.Annotation;
import java.util.Set;

@RequiredArgsConstructor
public class AnyOfValidator implements ConstraintValidator<AnyOf, Object> {

    private final Validator validator;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);

//        ValidationUtils.invokeValidator(validator, value, null);

        BeanDescriptor beanDescriptor = validator.getConstraintsForClass(value.getClass());
        Set<ConstraintDescriptor<?>> constraintDescriptors = beanDescriptor.getConstraintDescriptors();

        for (ConstraintDescriptor<?> constraintDescriptor : constraintDescriptors) {
            Annotation annotation = constraintDescriptor.getAnnotation();
            if (!(annotation instanceof AnyOf anyOf)) {
                continue;
            }

            Class<? extends Annotation> constraint = anyOf.constraint();
            String[] fieldNames = anyOf.fieldNames();

            for (Class<? extends ConstraintValidator<?, ?>> validatorType : constraintDescriptor.getConstraintValidatorClasses()) {

            }

        }

//        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(value);

        return true;
    }

}
