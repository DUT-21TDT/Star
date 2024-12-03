package com.pbl.star.validators.annotations;

import com.pbl.star.validators.FieldMatchValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldMatchValidator.class) // Chỉ định validator
@Target({ ElementType.TYPE }) // Áp dụng cho lớp
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldMatch {
    String message() default "Fields do not match"; // Thông báo lỗi mặc định
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    // Các tham số cho validator
    String first(); // Tên field đầu tiên
    String second(); // Tên field thứ hai

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        FieldMatch[] value(); // Cho phép áp dụng nhiều lần
    }
}
