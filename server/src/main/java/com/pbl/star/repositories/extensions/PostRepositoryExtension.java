package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.post.PostOverviewDTO;
import com.pbl.star.enums.PostStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface PostRepositoryExtension {
    Slice<PostOverviewDTO> findPostOverviewsByStatusAndUser(Pageable pageable, Instant after, PostStatus status, String userId);
    Slice<PostOverviewDTO> findPostOverviewsByStatusInRooms(Pageable pageable, Instant after, PostStatus status, String... roomIds);
}
