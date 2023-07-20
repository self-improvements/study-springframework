package io.github.imsejin.study.springframework.core.validation.constraint;

import io.github.imsejin.study.springframework.core.validation.validator.AnyOfValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Repeatable(AnyOf.List.class)
@Constraint(validatedBy = AnyOfValidator.class)
public @interface AnyOf {

    Class<? extends Annotation> constraint();

    String[] fieldNames();

    String message() default "No constraint is satisfied";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // -------------------------------------------------------------------------------------------------

    /*
     * Defines several {@link AnyOf} annotations on the same element.
     *
     * @see AnyOf
     */
    @Documented
    @Target(TYPE)
    @Retention(RUNTIME)
    @interface List {
        AnyOf[] value();
    }

}
