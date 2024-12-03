package com.pbl.star.validators;

import com.pbl.star.validators.annotations.DifferentFrom;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class DifferentFromValidator implements ConstraintValidator<DifferentFrom, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(DifferentFrom constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // No validation if the object itself is null
        }
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        Object firstValue = beanWrapper.getPropertyValue(firstFieldName);
        Object secondValue = beanWrapper.getPropertyValue(secondFieldName);

        // Validate: new password should not be the same as the old password
        return firstValue == null || !firstValue.equals(secondValue);
    }
}
