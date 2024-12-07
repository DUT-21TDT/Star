package com.pbl.star.services.domain;

import com.pbl.star.dtos.query.follow.FollowSectionCount;
import com.pbl.star.dtos.query.user.OnFollowProfile;
import com.pbl.star.dtos.query.user.OnFollowRequestProfile;
import com.pbl.star.dtos.query.user.OnSuggestionProfile;
import com.pbl.star.dtos.response.CustomSlice;
import com.pbl.star.entities.Following;
import com.pbl.star.enums.FollowRequestAction;

import java.time.Instant;
import java.util.List;

public interface FollowService {
    Following sendFollowRequest(String followerId, String followeeId);
    Following updateFollowRequestStatus(String userId, String followingId, FollowRequestAction action);
    Following removeFollowing(String followerId, String followeeId);
    void acceptAllFollowRequests(String userId);
    CustomSlice<OnFollowProfile> getFollowingsOfUser(String currentUserId, String targetUserId, int limit, Instant after);
    CustomSlice<OnFollowProfile> getFollowersOfUser(String currentUserId, String targetUserId, int limit, Instant after);
    CustomSlice<OnFollowRequestProfile> getFollowRequestsOfUser(String userId, int limit, Instant after);
    FollowSectionCount countFollowSection(String currentUserId, String targetUserId);
    List<OnSuggestionProfile> suggestFollow(String currentUserId, int limit);
}
