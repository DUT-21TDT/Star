package com.pbl.star.services;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface UserService {
    Collection<GrantedAuthority> getUserAuthorities(String username);
}
