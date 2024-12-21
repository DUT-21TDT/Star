package com.pbl.star.dtos.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUserStatusParams {
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(?i)(block|unblock)$", message = "Invalid status")
    private String status;
}
