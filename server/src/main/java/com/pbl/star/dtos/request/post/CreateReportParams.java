package com.pbl.star.dtos.request.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReportParams {
    @NotBlank(message = "Reason is required")
    private String reason;
}
