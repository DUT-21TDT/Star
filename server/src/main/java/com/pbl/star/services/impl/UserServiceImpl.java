package com.pbl.star.services.impl;

import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.query.user.PublicProfile;
import com.pbl.star.dtos.response.user.PublicProfileResponse;
import com.pbl.star.enums.UserRole;
import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.repositories.UserRepository;
import com.pbl.star.services.UserService;
import com.pbl.star.utils.AuthUtil;
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
    public PublicProfileResponse getPublicProfile(String username) {
        PublicProfile publicProfile = userRepository.getPublicProfile(username);

        if (publicProfile == null) {
            throw new EntityNotFoundException("User not found");
        }

        PublicProfileResponse publicProfileResponse = new PublicProfileResponse();
        publicProfileResponse.setPublicProfile(publicProfile);

        publicProfileResponse.setCurrentUser(username.equals(AuthUtil.getCurrentUser().getUsername()));

        // TODO: Replace with actual following status
        publicProfileResponse.setFollowing(false);

        return publicProfileResponse;
    }

    @Override
    public Collection<GrantedAuthority> getUserAuthorities(String userId) {
        UserRole role = userRepository.getRoleByUserId(userId);
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public PersonalInformation getPersonalInformation(String username) {
        PersonalInformation personalInformation = userRepository.getPersonalInformation(username);
        if (personalInformation == null) {
            throw new EntityNotFoundException("User not found");
        }

        return personalInformation;
    }
}
