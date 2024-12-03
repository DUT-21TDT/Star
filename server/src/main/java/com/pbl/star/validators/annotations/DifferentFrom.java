package com.pbl.star.validators.annotations;

import com.pbl.star.validators.DifferentFromValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DifferentFromValidator.class) // Specify the validator
@Target({ ElementType.TYPE }) // Apply to the class level
@Retention(RetentionPolicy.RUNTIME)
public @interface DifferentFrom {
    String message() default "Fields is the same"; // Default error message
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String first(); // Field for the old password
    String second(); // Field for the new password
}
