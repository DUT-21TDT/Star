package com.pbl.star.usecase.enduser;

import com.pbl.star.dtos.query.follow.FollowSectionCount;
import com.pbl.star.dtos.query.user.OnFollowProfile;
import com.pbl.star.dtos.query.user.OnFollowRequestProfile;
import com.pbl.star.dtos.query.user.OnSearchProfile;
import com.pbl.star.dtos.response.CustomSlice;
import com.pbl.star.dtos.response.user.FollowResponse;
import com.pbl.star.dtos.query.user.OnWallProfile;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface InteractUserUsecase {
    Slice<OnSearchProfile> searchUsers(String keyword, int limit, String afterId);
    OnWallProfile getProfileOnWall(String userId);
    CustomSlice<OnFollowProfile> getFollowers(String userId, int limit, Instant after);
    CustomSlice<OnFollowProfile> getFollowings(String userId, int limit, Instant after);
    CustomSlice<OnFollowRequestProfile> getFollowRequests(int limit, Instant after);
    FollowResponse followUser(String userId);
    void unfollowUser(String userId);
    void removeFollower(String userId);
    void acceptFollowRequest(String followingId);
    void rejectFollowRequest(String followingId);
    FollowSectionCount countFollowSection(String userId);
}
