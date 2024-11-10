package com.pbl.star.usecase;

import com.pbl.star.dtos.query.user.OnSearchProfile;
import com.pbl.star.dtos.response.user.FollowResponse;
import com.pbl.star.dtos.response.user.OnWallProfileResponse;
import org.springframework.data.domain.Slice;

public interface UserInteractUsecase {
    Slice<OnSearchProfile> searchUsers(String keyword, int limit, String afterId);
    OnWallProfileResponse getProfileOnWall(String userId);
    FollowResponse followUser(String userId);
//    void revokeFollowRequest(String userId);
    void acceptFollowRequest(String followingId);
    void rejectFollowRequest(String followingId);
    void unfollowUser(String userId);
}
