package com.pbl.star.utils;

import com.pbl.star.dtos.request.auth.SignUpParams;
import com.pbl.star.exceptions.InvalidSignUpParamsException;

import java.util.regex.Pattern;


public class AuthUtil {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z_](?!.*?\\.{2})[\\w.]{4,28}\\w$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");

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

    public static void validateSignUpRules(SignUpParams signUpParams) {
        validateUsername(signUpParams.getUsername());
        validatePassword(signUpParams.getPassword());
        validateConfirmPassword(signUpParams.getPassword(), signUpParams.getConfirmPassword());
    }

    private static void validateUsername(String username) {
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new InvalidSignUpFormException("Username must be between 6 and 30 characters long and can only contain letters, numbers, and special characters . _");
        }
    }

    private static void validatePassword(String password) {
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new InvalidSignUpFormException("Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, and one digit.");
        }
    }

    private static void validateConfirmPassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new InvalidSignUpParamsException("Password and confirm password do not match");
        }
    }
}
