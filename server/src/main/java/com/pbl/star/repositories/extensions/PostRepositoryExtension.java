package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.post.PendingPostForUserDTO;
import com.pbl.star.dtos.query.post.PostForModDTO;
import com.pbl.star.dtos.query.post.PostForUserDTO;
import com.pbl.star.enums.PostStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface PostRepositoryExtension {
    Slice<PostForUserDTO> findExistPostsOfUserByStatus(Pageable pageable, Instant after, PostStatus status, String userId);
    Slice<PendingPostForUserDTO> findExistPendingPostsOfUser(Pageable pageable, Instant after, String userId);
    Slice<PostForUserDTO> findExistPostsInRoomsByStatusAsUser(Pageable pageable, Instant after, PostStatus status, String... roomIds);
    Slice<PostForModDTO> findExistPostsInRoomByStatusAsMod(Pageable pageable, Instant after, PostStatus status, String roomId);
}
