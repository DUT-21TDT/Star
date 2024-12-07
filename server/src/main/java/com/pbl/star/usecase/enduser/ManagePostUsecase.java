package com.pbl.star.usecase.enduser;

import com.pbl.star.dtos.request.post.CreatePostParams;


public interface ManagePostUsecase {
    String createPost(CreatePostParams createPostParams);
    void deletePost(String postId);
}
