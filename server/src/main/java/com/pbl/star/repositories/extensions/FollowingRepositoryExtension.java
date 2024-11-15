package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.follow.FollowSectionCount;
import com.pbl.star.dtos.query.user.OnFollowProfile;
import com.pbl.star.dtos.query.user.OnFollowRequestProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface FollowingRepositoryExtension {
    Slice<OnFollowProfile> getFollowingsOfUser(Pageable pageable, Instant after, String currentUserId, String targetUserId);
    Slice<OnFollowProfile> getFollowersOfUser(Pageable pageable, Instant after, String currentUserId, String targetUserId);
    Slice<OnFollowRequestProfile> getFollowRequestsOfUser(Pageable pageable, Instant after, String userId);
    FollowSectionCount countFollowSection(String currentUserId, String targetUserId);
}
