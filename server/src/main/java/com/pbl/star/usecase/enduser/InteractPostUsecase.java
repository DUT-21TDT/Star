package com.pbl.star.usecase.enduser;

public interface InteractPostUsecase {
    void likePost(String postId);
    void unlikePost(String postId);
    void repostPost(String postId);
    void unRepostPost(String postId);
    void reportPost(String postId);
}
