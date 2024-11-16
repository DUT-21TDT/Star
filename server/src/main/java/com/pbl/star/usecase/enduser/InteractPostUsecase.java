package com.pbl.star.usecase.enduser;

import com.pbl.star.dtos.request.post.CreatePostParams;

public interface InteractPostUsecase {
    void likePost(String postId);
    void unlikePost(String postId);
    String replyPost(CreatePostParams createReplyParams);
    void repostPost(String postId);
    void unRepostPost(String postId);
    void reportPost(String postId);
}
