package com.pbl.star.services.domain;

import com.pbl.star.dtos.response.user.FollowResponse;
import com.pbl.star.enums.FollowRequestAction;

public interface FollowService {
    FollowResponse sendFollowRequest(String followerId, String followeeId);
    void updateFollowRequestStatus(String userId, String followingId, FollowRequestAction action);
    void removeFollowRequest(String followerId, String followeeId);
}
