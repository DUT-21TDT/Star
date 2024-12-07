package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.post.PendingPostForUserDTO;
import com.pbl.star.dtos.query.post.PostForModDTO;
import com.pbl.star.dtos.query.post.PostForUserDTO;
import com.pbl.star.dtos.query.post.ReplyOnWallDTO;
import com.pbl.star.enums.PostStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PostRepositoryExtension {
    List<PostForUserDTO> findExistPostsOfUserByStatusAsUser(int limit, Instant after, PostStatus status, String userId);
    List<PostForUserDTO> findExistPostsInRoomsByStatusAsUser(int limit, Instant after, PostStatus status, String... roomIds);
    Optional<PostForUserDTO> findExistPostByIdAsUser(String currentUserId, String postId);
    List<PostForUserDTO> findExistRepliesOfPostAsUser(int limit, Instant after, String currentUserId, String postId);
    List<ReplyOnWallDTO> findExistRepliesOnWallAsUser(int limit, Instant after, String currentUserId, String targetUserId);

    List<PendingPostForUserDTO> findExistPendingPostsOfUser(int limit, Instant after, String userId);
    // Mod
    List<PostForModDTO> findExistPostsInRoomByStatusAsMod(int limit, Instant after, PostStatus status, String roomId);
}
