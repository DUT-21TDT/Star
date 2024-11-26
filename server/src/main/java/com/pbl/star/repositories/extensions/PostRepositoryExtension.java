package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.post.PendingPostForUserDTO;
import com.pbl.star.dtos.query.post.PostForModDTO;
import com.pbl.star.dtos.query.post.PostForUserDTO;
import com.pbl.star.dtos.query.post.ReplyOnWallDTO;
import com.pbl.star.enums.PostStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.Instant;
import java.util.Optional;

public interface PostRepositoryExtension {
    Slice<PostForUserDTO> findExistPostsOfUserByStatusAsUser(Pageable pageable, Instant after, PostStatus status, String userId);
    Slice<PostForUserDTO> findExistPostsInRoomsByStatusAsUser(Pageable pageable, Instant after, PostStatus status, String... roomIds);
    Optional<PostForUserDTO> findExistPostByIdAsUser(String currentUserId, String postId);
    Slice<PostForUserDTO> findExistRepliesOfPostAsUser(Pageable pageable, Instant after, String currentUserId, String postId);
    Slice<ReplyOnWallDTO> findExistRepliesOnWallAsUser(Pageable pageable, Instant after, String currentUserId, String targetUserId);

    Slice<PendingPostForUserDTO> findExistPendingPostsOfUser(Pageable pageable, Instant after, String userId);
    // Mod
    Slice<PostForModDTO> findExistPostsInRoomByStatusAsMod(Pageable pageable, Instant after, PostStatus status, String roomId);
}
