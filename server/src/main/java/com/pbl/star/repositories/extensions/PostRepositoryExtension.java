package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.post.PostOverviewDTO;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface PostRepositoryExtension {
    Slice<PostOverviewDTO> findPostOverviewsByUser(String userId, int limit, Instant after);
}
