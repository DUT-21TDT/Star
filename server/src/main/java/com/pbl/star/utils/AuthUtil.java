package com.pbl.star.utils;

import com.pbl.star.models.projections.user.BasicUserInfo;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    public static BasicUserInfo getCurrentUser() {
        return (BasicUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
