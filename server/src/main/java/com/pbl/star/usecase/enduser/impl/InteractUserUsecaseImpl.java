package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.dtos.response.PaginationSlice;
import com.pbl.star.dtos.response.follow.FollowCountResponse;
import com.pbl.star.dtos.response.follow.FollowResponse;
import com.pbl.star.dtos.response.user.OnFollowProfileResponse;
import com.pbl.star.dtos.response.user.OnFollowReqProfileResponse;
import com.pbl.star.dtos.response.user.OnSearchProfileResponse;
import com.pbl.star.dtos.response.user.OnWallProfileResponse;
import com.pbl.star.enums.FollowRequestAction;
import com.pbl.star.enums.FollowRequestStatus;
import com.pbl.star.enums.FollowStatus;
import com.pbl.star.mapper.follow.FollowDTOMapper;
import com.pbl.star.mapper.user.UserDTOMapper;
import com.pbl.star.models.entities.Following;
import com.pbl.star.models.projections.user.OnWallProfile;
import com.pbl.star.services.domain.FollowService;
import com.pbl.star.services.domain.UserService;
import com.pbl.star.services.external.NotificationProducer;
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

    private final NotificationProducer notificationProducer;

    private final UserDTOMapper userMapper;
    private final FollowDTOMapper followMapper;

    @Override
    public Slice<OnSearchProfileResponse> searchUsers(String keyword, int limit, String afterId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return userService.searchUsers(currentUserId, keyword, limit, afterId)
                .map(userMapper::toDTO);
    }

    @Override
    public OnWallProfileResponse getProfileOnWall(String userId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        OnWallProfile profile = userService.getProfileOnWall(currentUserId, userId);
        return userMapper.toDTO(profile);
    }

    @Override
    public PaginationSlice<OnFollowProfileResponse> getFollowers(String userId, int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return followService.getFollowersOfUser(currentUserId, userId, limit, after)
                .map(userMapper::toDTO);
    }

    @Override
    public PaginationSlice<OnFollowProfileResponse> getFollowings(String userId, int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return followService.getFollowingsOfUser(currentUserId, userId, limit, after)
                .map(userMapper::toDTO);
    }

    @Override
    public PaginationSlice<OnFollowReqProfileResponse> getFollowRequests(int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return followService.getFollowRequestsOfUser(currentUserId, limit, after)
                .map(userMapper::toDTO);
    }

    @Override
    public FollowResponse followUser(String followeeId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        Following savedFollowing = followService.sendFollowRequest(currentUserId, followeeId);

        FollowResponse response = FollowResponse.builder()
                .id(savedFollowing.getId())
                .followStatus(savedFollowing.getStatus() == FollowRequestStatus.ACCEPTED ?
                        FollowStatus.FOLLOWING : FollowStatus.REQUESTED
                )
                .build();

        if (response.getFollowStatus() == FollowStatus.FOLLOWING) {
            notificationProducer.pushFollowMessage(savedFollowing);
        } else {
            notificationProducer.pushRequestFollowMessage(savedFollowing);
        }

        return response;
    }

    @Override
    public void unfollowUser(String followeeId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        Following deletedFollowing = followService.removeFollowing(currentUserId, followeeId);

        if (deletedFollowing.getStatus().equals(FollowRequestStatus.ACCEPTED)) {
            notificationProducer.pushUnfollowMessage(deletedFollowing);
        } else {
            notificationProducer.pushRevokeRequestFollowMessage(deletedFollowing);
        }
    }

    @Override
    public void removeFollower(String userId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        followService.removeFollowing(userId, currentUserId);
    }

    @Override
    public void acceptFollowRequest(String followingId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        Following savedFollowing = followService.updateFollowRequestStatus(currentUserId, followingId, FollowRequestAction.ACCEPT);

        notificationProducer.pushAcceptFollowMessage(savedFollowing);
    }

    @Override
    public void rejectFollowRequest(String followingId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        followService.updateFollowRequestStatus(currentUserId, followingId, FollowRequestAction.REJECT);
    }

    @Override
    public FollowCountResponse countFollowSection(String userId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return followMapper.toDTO(followService.countFollowSection(currentUserId, userId));
    }
}
