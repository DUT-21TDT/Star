package com.pbl.star.dtos.request.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RejectPostParams {
    @NotBlank(message = "Reason is required")
    @Size(min = 1, max = 255, message = "Reason must be between 1 and 255 characters")
    private String reason;
}
