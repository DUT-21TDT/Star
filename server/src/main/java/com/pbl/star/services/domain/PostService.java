package com.pbl.star.services.domain;

import com.pbl.star.dtos.query.post.PostOverviewDTO;
import com.pbl.star.dtos.request.post.CreatePostParams;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface PostService {
    String createPost(String userId, CreatePostParams createPostParams);
    Slice<PostOverviewDTO> getPostsOnUserWall(String userId, int limit, Instant after);
    Slice<PostOverviewDTO> getPostsOnNewsfeed(String userId, int limit, Instant after);
    void likePost(String userId, String postId);
    void unlikePost(String userId, String postId);
}
