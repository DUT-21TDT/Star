package com.pbl.star.usecase.enduser;

import com.pbl.star.dtos.response.PaginationSlice;
import com.pbl.star.dtos.response.follow.FollowCountResponse;
import com.pbl.star.dtos.response.follow.FollowResponse;
import com.pbl.star.dtos.response.user.OnFollowProfileResponse;
import com.pbl.star.dtos.response.user.OnFollowReqProfileResponse;
import com.pbl.star.dtos.response.user.OnSearchProfileResponse;
import com.pbl.star.dtos.response.user.OnWallProfileResponse;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface InteractUserUsecase {
    Slice<OnSearchProfileResponse> searchUsers(String keyword, int limit, String afterId);
    OnWallProfileResponse getProfileOnWall(String userId);
    PaginationSlice<OnFollowProfileResponse> getFollowers(String userId, int limit, Instant after);
    PaginationSlice<OnFollowProfileResponse> getFollowings(String userId, int limit, Instant after);
    PaginationSlice<OnFollowReqProfileResponse> getFollowRequests(int limit, Instant after);
    FollowResponse followUser(String userId);
    void unfollowUser(String userId);
    void removeFollower(String userId);
    void acceptFollowRequest(String followingId);
    void rejectFollowRequest(String followingId);
    FollowCountResponse countFollowSection(String userId);
}
