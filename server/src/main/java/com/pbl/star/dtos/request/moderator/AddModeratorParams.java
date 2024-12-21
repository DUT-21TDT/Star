package com.pbl.star.dtos.request.moderator;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddModeratorParams {
    @NotBlank(message = "User ID is required")
    private String userId;
}
