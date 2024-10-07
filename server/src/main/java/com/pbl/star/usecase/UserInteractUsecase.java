package com.pbl.star.usecase;

import com.pbl.star.dtos.response.user.PublicProfileResponse;

public interface UserInteractUsecase {
    PublicProfileResponse getPublicProfile(String userId);
}
