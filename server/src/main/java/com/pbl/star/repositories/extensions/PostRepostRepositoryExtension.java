package com.pbl.star.repositories.extensions;

import com.pbl.star.models.projections.post.RepostOnWall;

import java.time.Instant;
import java.util.List;

public interface PostRepostRepositoryExtension {
    List<RepostOnWall> findRepostsOnWallAsUser(int limit, Instant after, String currentUserId, String targetUserId);
}
