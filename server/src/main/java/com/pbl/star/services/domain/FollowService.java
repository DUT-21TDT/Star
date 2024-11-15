package com.pbl.star.services.domain;

import com.pbl.star.dtos.query.user.OnFollowProfile;
import com.pbl.star.dtos.query.user.OnFollowRequestProfile;
import com.pbl.star.dtos.response.CustomSlice;
import com.pbl.star.dtos.response.user.FollowResponse;
import com.pbl.star.enums.FollowRequestAction;

import java.time.Instant;

public interface FollowService {
    FollowResponse sendFollowRequest(String followerId, String followeeId);
    void updateFollowRequestStatus(String userId, String followingId, FollowRequestAction action);
    void removeFollowing(String followerId, String followeeId);
    void acceptAllFollowRequests(String userId);
    CustomSlice<OnFollowProfile> getFollowingsOfUser(String currentUserId, String targetUserId, int limit, Instant after);
    CustomSlice<OnFollowProfile> getFollowersOfUser(String currentUserId, String targetUserId, int limit, Instant after);
    CustomSlice<OnFollowRequestProfile> getFollowRequestsOfUser(String userId, int limit, Instant after);
}
