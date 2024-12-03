package com.pbl.star.dtos.request.user;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminGetUsersParams {
    @Size(max = 50, message = "Keyword cannot exceed 50 characters")
    private String keyword;

    @Pattern(regexp = "^(ACTIVE|INACTIVE|BLOCKED)$", message = "Status must be ACTIVE, INACTIVE, or BLOCKED")
    private String status;

    private String sortBy = "username";

    @Pattern(regexp = "^(?i)(asc|desc)$", message = "Direction must be 'asc' or 'desc'")
    private String direction = "asc";

    @Min(value = 0, message = "Page cannot be negative")
    private int page = 0;

    @Min(value = 1, message = "Size must be at least 1")
    private int size = 10;
}
