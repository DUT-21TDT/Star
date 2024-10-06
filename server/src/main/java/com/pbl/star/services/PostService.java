package com.pbl.star.services;

import com.pbl.star.dtos.query.post.PostOverviewDTO;
import com.pbl.star.dtos.request.post.CreatePostParams;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface PostService {
    String createPost(String userId, CreatePostParams createPostParams);
    Slice<PostOverviewDTO> getPostsByUser(String userId, int limit, Instant after);
}
