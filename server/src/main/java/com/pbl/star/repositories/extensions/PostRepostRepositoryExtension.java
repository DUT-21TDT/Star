package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.post.RepostOnWallDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface PostRepostRepositoryExtension {
    Slice<RepostOnWallDTO> findRepostsOnWallAsUser(Pageable pageable, Instant after, String currentUserId, String targetUserId);
}
