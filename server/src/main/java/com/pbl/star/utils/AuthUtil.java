package com.pbl.star.utils;

import com.pbl.star.dtos.query.user.GeneralInformation;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    public static GeneralInformation getCurrentUser() {
        return (GeneralInformation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static boolean isCurrentUser(String userId) {
        return getCurrentUser().getId().equals(userId);
    }
}
