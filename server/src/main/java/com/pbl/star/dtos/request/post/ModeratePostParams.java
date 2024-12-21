package com.pbl.star.dtos.request.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModeratePostParams {
    @NotNull(message = "Status is required")
    @Pattern(regexp = "^(?i)(approved|rejected|pending)$", message = "Invalid status")
    private String status;
}
