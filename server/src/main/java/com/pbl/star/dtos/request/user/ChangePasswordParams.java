package com.pbl.star.dtos.request.user;

import com.pbl.star.validators.annotations.DifferentFrom;
import com.pbl.star.validators.annotations.FieldMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FieldMatch(first = "newPassword", second = "confirmNewPassword",
        message = "Passwords must match")
@DifferentFrom(first = "oldPassword", second = "newPassword",
        message = "New password must not be the same as the old password")
public class ChangePasswordParams {
    @NotBlank(message = "Old password is required")
    private String oldPassword;

    @NotBlank
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",
            message = "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, and one digit.")
    private String newPassword;

    @NotBlank(message = "Confirm new password is required")
    private String confirmNewPassword;
}
