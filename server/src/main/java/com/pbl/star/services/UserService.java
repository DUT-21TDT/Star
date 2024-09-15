package com.pbl.star.services;

import com.pbl.star.dtos.response.user.PublicProfileResponse;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface UserService {
    PublicProfileResponse getPublicProfile(String username);
    Collection<GrantedAuthority> getUserAuthorities(String username);
}