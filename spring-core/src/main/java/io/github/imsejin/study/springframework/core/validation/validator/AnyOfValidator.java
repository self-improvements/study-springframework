package io.github.imsejin.study.springframework.core.validation.validator;

import io.github.imsejin.study.springframework.core.validation.constraint.AnyOf;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.Validator;
import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.ConstraintDescriptor;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorDescriptor;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

public class AnyOfValidator implements ConstraintValidator<AnyOf, Object> {

    /**
     * @see org.hibernate.validator.internal.engine.ValidatorFactoryImpl
     */
    private static final ConstraintHelper constraintHelper = ConstraintHelper.forAllBuiltinConstraints();

    private final Validator validator;

    private final ConstraintValidatorFactory factory;

    /**
     * @see LocalValidatorFactoryBean#afterPropertiesSet
     */
    private AnyOfValidator(Validator validator, ApplicationContext context) {
        this.validator = validator;
        this.factory = new SpringConstraintValidatorFactory(context.getAutowireCapableBeanFactory());
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);

        BeanDescriptor beanDescriptor = validator.getConstraintsForClass(value.getClass());
        Set<ConstraintDescriptor<?>> constraintDescriptors = beanDescriptor.getConstraintDescriptors();

        // Validates as much as the number of @AnyOf annotated on the target class: AND condition
        boolean valid = true;

        for (ConstraintDescriptor<?> constraintDescriptor : constraintDescriptors) {
            Annotation annotation = constraintDescriptor.getAnnotation();
            if (!(annotation instanceof AnyOf anyOf)) {
                continue;
            }

            Class<? extends Annotation> constraint = anyOf.constraint();
            String[] fieldNames = anyOf.fieldNames();

            if (fieldNames.length == 0) {
                throw new IllegalStateException("@AnyOf.fieldNames must have one or more elements: " + value.getClass());
            }

            List<? extends ConstraintValidatorDescriptor> constraintValidatorDescriptors =
                    constraintHelper.getAllValidatorDescriptors(constraint);

            // Validates as much as @AnyOf.fieldNames.length: OR condition
            boolean isValidField = false;

            for (String fieldName : fieldNames) {
                Object fieldValue = beanWrapper.getPropertyValue(fieldName);

                // Validates as much as the number of ConstraintValidator mapped with constraint annotation: AND condition
                boolean satisfiedAll = true;

                for (ConstraintValidatorDescriptor constraintValidatorDescriptor : constraintValidatorDescriptors) {
                    Type validatedType = constraintValidatorDescriptor.getValidatedType();

                    // Resolves a proper ConstraintValidator for the validated object, but null is always required to be validated.
                    if (fieldValue != null && !((Class<?>) validatedType).isInstance(fieldValue)) {
                        continue;
                    }

                    ConstraintValidator constraintValidator = constraintValidatorDescriptor.newInstance(factory);
                    satisfiedAll &= constraintValidator.isValid(fieldValue, context);
                }

                // Stops redundant validation.
                if (satisfiedAll) {
                    isValidField = true;
                    break;
                }
            }

            valid &= isValidField;
        }

        return valid;
    }

}
