package com.pbl.star.usecase;

import com.pbl.star.dtos.request.post.CreatePostParams;

public interface PostManageUsecase {
    String createPost(CreatePostParams createPostParams);
}
