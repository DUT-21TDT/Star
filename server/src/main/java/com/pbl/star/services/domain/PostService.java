package com.pbl.star.services.domain;

import com.pbl.star.dtos.query.post.*;
import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.dtos.response.CustomSlice;
import com.pbl.star.entities.Post;
import com.pbl.star.enums.PostStatus;
import org.springframework.data.domain.Slice;

import java.time.Instant;
import java.util.Optional;

public interface PostService {
    Post createPost(String userId, CreatePostParams createPostParams);
    PostForUserDTO getPostById(String currentUserId, String postId);
    Slice<PostForUserDTO> getPostsOnUserWall(String currentUserId, String targetUserId, int limit, Instant after);
    Slice<PendingPostForUserDTO> getPendingPostsByUser(String userId, int limit, Instant after);
    Slice<PostForUserDTO> getPostsOnNewsfeed(String currentUserId, int limit, Instant after);
    Slice<PostForUserDTO> getPostsInRoom(String roomId, PostStatus status, int limit, Instant after);
    Slice<PostForModDTO> getPostsInRoomAsMod(String roomId, PostStatus status, int limit, Instant after);
    Post moderatePostStatus(String postId, PostStatus status, String moderatorId);
    void unmoderatePostStatus(String postId, String moderatorId);
    Post deletePostOfUser(String postId, String userId);
    Post createReply(String userId, CreatePostParams createReplyParams);
    CustomSlice<PostForUserDTO> getReplies(String userId, String postId, int limit, Instant after);
    Slice<ReplyOnWallDTO> getRepliesOnWall(String currentUserId, String targetUserId, int limit, Instant after);
    Slice<RepostOnWallDTO> getRepostsOnWall(String currentUserId, String targetUserId, int limit, Instant after);

    // Repository methods
    Optional<Post> findExistPostById(String postId);
}
