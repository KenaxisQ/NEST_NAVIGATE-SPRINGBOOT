package com.kenaxisq.nestnavigate.property.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class RequiredFieldValidator implements ConstraintValidator<RequiredField, Object> {

    @Override
    public void initialize(RequiredField constraintAnnotation) {
        // Initialization logic if required (not needed here)
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        // Check for non-null, non-empty fields
        Field[] fields = value.getClass().getDeclaredFields();
        boolean isValid = true;
        for (Field field : fields) {
            // For fields annotated with @RequiredField
            if (field.isAnnotationPresent(RequiredField.class)) {
                try {
                    field.setAccessible(true);
                    Object fieldValue = field.get(value);

                    if (fieldValue == null || (fieldValue instanceof String && ((String) fieldValue).isEmpty())) {
                        // If the field is null or empty, add a validation error message
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(field.getName() + " is required")
                                .addPropertyNode(field.getName())
                                .addConstraintViolation();
                        isValid = false;
                    }
                } catch (IllegalAccessException e) {
                    // Handle exception if needed
                    e.printStackTrace();
                }
            }
        }
        return isValid;
    }
}

