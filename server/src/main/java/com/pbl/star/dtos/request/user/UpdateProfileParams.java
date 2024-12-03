package com.pbl.star.dtos.request.user;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileParams {
    @NotBlank(message = "Username cannot be empty")
    @Pattern(regexp = "^[a-zA-Z_](?!.*?\\.{2})[\\w.]{4,28}\\w$",
            message = "Username must be between 6 and 30 characters long and can only contain letters, numbers, and special characters . _")
    private String username;

    @Size(max = 50, message = "First name must be at most 50 characters long")
    private String firstName;

    @Size(max = 50, message = "Last name must be at most 50 characters long")
    private String lastName;

    @Size(max = 200, message = "Bio must be at most 200 characters long")
    private String bio;

    private String avatarFileName;
    private String dateOfBirth;

    @Pattern(regexp = "^(MALE|FEMALE)$", message = "Gender must be either MALE or FEMALE")
    private String gender;

    @NotNull(message = "Private profile cannot be null")
    private boolean privateProfile;
}
