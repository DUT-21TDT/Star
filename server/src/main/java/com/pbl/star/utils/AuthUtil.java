package com.pbl.star.utils;

import com.pbl.star.enums.UserRole;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    public static CurrentUser getCurrentUser() {
        return CurrentUser.builder()
                .username(SecurityContextHolder.getContext().getAuthentication().getName())
                .role(UserRole.valueOf(
                        SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst().get().getAuthority())
                )
                .build();
    }
}
