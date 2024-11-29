package com.kenaxisq.nestnavigate.property.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RequiredFieldValidator.class) // Link to the custom validator
public @interface RequiredField {
    String message() default "This field is required"; // Default error message
    Class<?>[] groups() default {}; // Grouping for validation (optional)
    Class<? extends Payload>[] payload() default {}; // Custom data for the annotation (optional)
}

