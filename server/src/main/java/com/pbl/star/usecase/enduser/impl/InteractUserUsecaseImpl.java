package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.dtos.query.follow.FollowSectionCount;
import com.pbl.star.dtos.query.user.OnFollowProfile;
import com.pbl.star.dtos.query.user.OnFollowRequestProfile;
import com.pbl.star.dtos.query.user.OnSearchProfile;
import com.pbl.star.dtos.response.CustomSlice;
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

import java.time.Instant;

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
    public CustomSlice<OnFollowProfile> getFollowers(String userId, int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return followService.getFollowersOfUser(currentUserId, userId, limit, after);
    }

    @Override
    public CustomSlice<OnFollowProfile> getFollowings(String userId, int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return followService.getFollowingsOfUser(currentUserId, userId, limit, after);
    }

    @Override
    public CustomSlice<OnFollowRequestProfile> getFollowRequests(int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return followService.getFollowRequestsOfUser(currentUserId, limit, after);
    }

    @Override
    public FollowResponse followUser(String followeeId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return followService.sendFollowRequest(currentUserId, followeeId);
    }

    @Override
    public void unfollowUser(String followeeId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        followService.removeFollowing(currentUserId, followeeId);
    }

    @Override
    public void removeFollower(String userId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        followService.removeFollowing(userId, currentUserId);
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

    @Override
    public FollowSectionCount countFollowSection(String userId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return followService.countFollowSection(currentUserId, userId);
    }
}
