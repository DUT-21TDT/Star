package com.pbl.star.usecase.impl;

import com.pbl.star.dtos.response.user.FollowResponse;
import com.pbl.star.dtos.response.user.PublicProfileResponse;
import com.pbl.star.enums.FollowRequestAction;
import com.pbl.star.services.domain.UserService;
import com.pbl.star.usecase.UserInteractUsecase;
import com.pbl.star.utils.AuthUtil;
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

    @Override
    public FollowResponse followUser(String followeeId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return userService.sendFollowRequest(currentUserId, followeeId);
    }

    @Override
    public void unfollowUser(String followeeId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        userService.removeFollowRequest(currentUserId, followeeId);
    }

    @Override
    public void acceptFollowRequest(String followingId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        userService.updateFollowRequestStatus(currentUserId, followingId, FollowRequestAction.ACCEPT);
    }

    @Override
    public void rejectFollowRequest(String followingId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        userService.updateFollowRequestStatus(currentUserId, followingId, FollowRequestAction.REJECT);
    }
}
