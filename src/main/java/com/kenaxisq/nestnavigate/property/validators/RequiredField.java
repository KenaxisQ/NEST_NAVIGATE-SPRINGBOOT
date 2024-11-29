package com.kenaxisq.nestnavigate.property.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD}) // Defines where the annotation can be applied
@Retention(RetentionPolicy.RUNTIME) // The annotation will be available at runtime
@Constraint(validatedBy = RequiredFieldValidator.class) // Specifies the validator that will handle this constraint
public @interface RequiredField {
    String message() default "This field is required"; // Provides a default error message

    Class<?>[] groups() default {}; // Allows categorization of the constraint

    Class<? extends Payload>[] payload() default {}; // Can be used to carry metadata information
}