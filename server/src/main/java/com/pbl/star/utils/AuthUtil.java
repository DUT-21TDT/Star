package com.pbl.star.utils;

import com.pbl.star.dtos.request.SignUpParams;
import com.pbl.star.exceptions.InvalidSignUpFormException;


public class AuthUtil {
    public static void validateSignupRequiredFields(SignUpParams signUpParams) {
        if (signUpParams.getUsername() == null || signUpParams.getUsername().isBlank()) {
            throw new InvalidSignUpFormException("Username is required");
        }

        if (signUpParams.getEmail() == null || signUpParams.getEmail().isBlank()) {
            throw new InvalidSignUpFormException("Email is required");
        }

        if (signUpParams.getPassword() == null || signUpParams.getPassword().isBlank()) {
            throw new InvalidSignUpFormException("Password is required");
        }

        if (signUpParams.getConfirmPassword() == null || signUpParams.getConfirmPassword().isBlank()) {
            throw new InvalidSignUpFormException("Confirm password is required");
        }
    }

    public static void validateConfirmPassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new InvalidSignUpFormException("Password and confirm password do not match");
        }
    }
}
