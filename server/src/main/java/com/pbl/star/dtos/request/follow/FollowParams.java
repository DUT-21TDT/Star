package com.pbl.star.dtos.request.follow;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowParams {
    @NotBlank(message = "userId is required")
    private String userId;
}
