package com.pbl.star.services.impl;

import com.pbl.star.enums.UserRole;
import com.pbl.star.repositories.UserRepository;
import com.pbl.star.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Collection<GrantedAuthority> getUserAuthorities(String username) {
        UserRole role = userRepository.getRoleByUsername(username);
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
