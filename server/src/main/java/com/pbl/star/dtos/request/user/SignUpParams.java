package com.pbl.star.dtos.request.user;

import com.pbl.star.validators.annotations.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@FieldMatch(first = "password", second = "confirmPassword", message = "Passwords must match")
public class SignUpParams {
    @NotNull
    @Pattern(regexp = "^[a-zA-Z_](?!.*?\\.{2})[\\w.]{4,28}\\w$",
            message = "Username must be between 6 and 30 characters long and can only contain letters, numbers, and special characters . _")
    private String username;

    @NotNull
    @Email(message = "Invalid email address")
    @Size(max = 200, message = "Email address should be less than 200 characters")
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",
            message = "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, and one digit.")
    private String password;

    @NotNull
    private String confirmPassword;
}
