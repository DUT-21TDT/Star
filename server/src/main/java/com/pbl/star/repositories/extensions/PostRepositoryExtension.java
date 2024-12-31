package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.request.post.FilterPostParams;
import com.pbl.star.enums.PostStatus;
import com.pbl.star.models.projections.post.*;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PostRepositoryExtension {
    List<PostForAdmin> findExistPostsAsAdmin(Pageable pageable, FilterPostParams filter);
    long countExistPostsAsAdmin(FilterPostParams filter);
    List<PostForUser> findExistPostsOfUsersByStatusAsUser(int limit, Instant after, PostStatus status, List<String> userIds);
    List<LikedPostForUser> findExistPostsLikedByUserAndStatus(int limit, Instant after, PostStatus status, String currentUserId);
    List<PostForUser> findExistPostsInRoomsByStatusAsUser(int limit, Instant after, PostStatus status, List<String> roomIds);
    Optional<PostForUser> findExistPostByIdAsUser(String currentUserId, String postId);
    List<PostForUser> findExistRepliesOfPostAsUser(int limit, Instant after, String currentUserId, String postId);
    List<ReplyOnWall> findExistRepliesOnWallAsUser(int limit, Instant after, String currentUserId, String targetUserId);

    List<PendingPostForUser> findExistPendingPostsOfUser(int limit, Instant after, String userId);
    // Mod
    List<PostForMod> findExistPostsInRoomByStatusAsMod(int limit, Instant after, PostStatus status, String roomId);
}
