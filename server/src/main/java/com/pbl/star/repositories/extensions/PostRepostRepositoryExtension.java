package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.post.RepostOnWallDTO;

import java.time.Instant;
import java.util.List;

public interface PostRepostRepositoryExtension {
    List<RepostOnWallDTO> findRepostsOnWallAsUser(int limit, Instant after, String currentUserId, String targetUserId);
}
