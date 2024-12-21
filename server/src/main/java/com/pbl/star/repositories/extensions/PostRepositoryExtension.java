package com.pbl.star.repositories.extensions;

import com.pbl.star.models.projections.post.PendingPostForUser;
import com.pbl.star.models.projections.post.PostForMod;
import com.pbl.star.models.projections.post.PostForUser;
import com.pbl.star.models.projections.post.ReplyOnWall;
import com.pbl.star.enums.PostStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PostRepositoryExtension {
    List<PostForUser> findExistPostsOfUsersByStatusAsUser(int limit, Instant after, PostStatus status, List<String> userIds);
    List<PostForUser> findExistPostsInRoomsByStatusAsUser(int limit, Instant after, PostStatus status, List<String> roomIds);
    Optional<PostForUser> findExistPostByIdAsUser(String currentUserId, String postId);
    List<PostForUser> findExistRepliesOfPostAsUser(int limit, Instant after, String currentUserId, String postId);
    List<ReplyOnWall> findExistRepliesOnWallAsUser(int limit, Instant after, String currentUserId, String targetUserId);

    List<PendingPostForUser> findExistPendingPostsOfUser(int limit, Instant after, String userId);
    // Mod
    List<PostForMod> findExistPostsInRoomByStatusAsMod(int limit, Instant after, PostStatus status, String roomId);
}
