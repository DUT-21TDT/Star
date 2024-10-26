package com.pbl.star.services.domain;

import com.pbl.star.dtos.query.post.PostForModDTO;
import com.pbl.star.dtos.query.post.PostForUserDTO;
import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.enums.PostStatus;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface PostService {
    String createPost(String userId, CreatePostParams createPostParams);
    Slice<PostForUserDTO> getPostsOnUserWall(String userId, int limit, Instant after);
    Slice<PostForUserDTO> getPostsOnNewsfeed(String userId, int limit, Instant after);
    Slice<PostForUserDTO> getPostsInRoom(String roomId, PostStatus status, int limit, Instant after);
    Slice<PostForModDTO> getPostsInRoomAsMod(String roomId, PostStatus status, int limit, Instant after);
    void likePost(String userId, String postId);
    void unlikePost(String userId, String postId);
}
