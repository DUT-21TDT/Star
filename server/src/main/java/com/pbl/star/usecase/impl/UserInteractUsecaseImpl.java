package com.pbl.star.usecase.impl;

import com.pbl.star.dtos.response.user.PublicProfileResponse;
import com.pbl.star.services.domain.UserService;
import com.pbl.star.usecase.UserInteractUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInteractUsecaseImpl implements UserInteractUsecase {

    private final UserService userService;

    @Override
    public PublicProfileResponse getPublicProfile(String userId) {
        return userService.getPublicProfile(userId);
    }
}
