package com.pbl.star.dtos.request.post;

import com.pbl.star.enums.PostStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModeratePostParams {
    @NotBlank(message = "Status is required")
    private PostStatus status;
}
