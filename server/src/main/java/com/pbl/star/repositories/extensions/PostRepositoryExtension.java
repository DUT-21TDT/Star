package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.post.PostForModDTO;
import com.pbl.star.dtos.query.post.PostForUserDTO;
import com.pbl.star.enums.PostStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface PostRepositoryExtension {
    Slice<PostForUserDTO> findPostsOfUserByStatus(Pageable pageable, Instant after, PostStatus status, String userId);
    Slice<PostForUserDTO> findPostsInRoomsByStatusAsUser(Pageable pageable, Instant after, PostStatus status, String... roomIds);
    Slice<PostForModDTO> findPostsInRoomByStatusAsMod(Pageable pageable, Instant after, PostStatus status, String roomId);
}
