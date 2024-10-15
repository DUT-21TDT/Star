package com.pbl.star.utils;

import com.pbl.star.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CurrentUser {
    private String id;
    private UserRole role;
}
