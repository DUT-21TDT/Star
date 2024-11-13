package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.dtos.query.user.OnSearchProfile;
import com.pbl.star.dtos.response.user.FollowResponse;
import com.pbl.star.dtos.response.user.OnWallProfileResponse;
import com.pbl.star.enums.FollowRequestAction;
import com.pbl.star.services.domain.FollowService;
import com.pbl.star.services.domain.UserService;
import com.pbl.star.usecase.enduser.InteractUserUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InteractUserUsecaseImpl implements InteractUserUsecase {

    private final UserService userService;
    private final FollowService followService;

    @Override
    public Slice<OnSearchProfile> searchUsers(String keyword, int limit, String afterId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return userService.searchUsers(currentUserId, keyword, limit, afterId);
    }

    @Override
    public OnWallProfileResponse getProfileOnWall(String userId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return userService.getProfileOnWall(currentUserId, userId);
    }

    @Override
    public FollowResponse followUser(String followeeId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return followService.sendFollowRequest(currentUserId, followeeId);
    }

    @Override
    public void unfollowUser(String followeeId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        followService.removeFollowRequest(currentUserId, followeeId);
    }

    @Override
    public void acceptFollowRequest(String followingId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        followService.updateFollowRequestStatus(currentUserId, followingId, FollowRequestAction.ACCEPT);
    }

    @Override
    public void rejectFollowRequest(String followingId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        followService.updateFollowRequestStatus(currentUserId, followingId, FollowRequestAction.REJECT);
    }
}
