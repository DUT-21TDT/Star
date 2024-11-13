package com.pbl.star.services.domain;

import com.pbl.star.dtos.query.post.PendingPostForUserDTO;
import com.pbl.star.dtos.query.post.PostForModDTO;
import com.pbl.star.dtos.query.post.PostForUserDTO;
import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.enums.PostStatus;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface PostService {
    String createPost(String userId, CreatePostParams createPostParams);
    Slice<PostForUserDTO> getPostsOnUserWall(String currentUserId, String targetUserId, int limit, Instant after);
    Slice<PendingPostForUserDTO> getPendingPostsByUser(String userId, int limit, Instant after);
    Slice<PostForUserDTO> getPostsOnNewsfeed(String currentUserId, int limit, Instant after);
    Slice<PostForUserDTO> getPostsInRoom(String roomId, PostStatus status, int limit, Instant after);
    Slice<PostForModDTO> getPostsInRoomAsMod(String roomId, PostStatus status, int limit, Instant after);
    void moderatePostStatus(String postId, PostStatus status, String moderatorId);
    void unmoderatePostStatus(String postId, String moderatorId);
    void deletePostOfUser(String postId, String userId);
}
