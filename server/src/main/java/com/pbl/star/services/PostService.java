package com.pbl.star.services;

import com.pbl.star.dtos.request.post.CreatePostParams;

public interface PostService {
    String createPost(String userId, CreatePostParams createPostParams);
}
