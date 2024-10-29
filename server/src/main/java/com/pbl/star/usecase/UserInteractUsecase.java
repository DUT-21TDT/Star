package com.pbl.star.usecase;

import com.pbl.star.dtos.response.user.FollowResponse;
import com.pbl.star.dtos.response.user.PublicProfileResponse;

public interface UserInteractUsecase {
    PublicProfileResponse getPublicProfile(String userId);
    FollowResponse followUser(String userId);
//    void revokeFollowRequest(String userId);
    void acceptFollowRequest(String followingId);
    void rejectFollowRequest(String followingId);
    void unfollowUser(String userId);
}
