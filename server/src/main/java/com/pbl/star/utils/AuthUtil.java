package com.pbl.star.utils;

import com.pbl.star.dtos.request.auth.SignUpParams;
import com.pbl.star.exceptions.InvalidSignUpParamsException;


public class AuthUtil {
    public static void validateSignupRequiredFields(SignUpParams signUpParams) {
        if (signUpParams.getUsername() == null || signUpParams.getUsername().isBlank()) {
            throw new InvalidSignUpParamsException("Username is required");
        }

        if (signUpParams.getEmail() == null || signUpParams.getEmail().isBlank()) {
            throw new InvalidSignUpParamsException("Email is required");
        }

        if (signUpParams.getPassword() == null || signUpParams.getPassword().isBlank()) {
            throw new InvalidSignUpParamsException("Password is required");
        }

        if (signUpParams.getConfirmPassword() == null || signUpParams.getConfirmPassword().isBlank()) {
            throw new InvalidSignUpParamsException("Confirm password is required");
        }
    }

    public static void validateConfirmPassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new InvalidSignUpParamsException("Password and confirm password do not match");
        }
    }
}
