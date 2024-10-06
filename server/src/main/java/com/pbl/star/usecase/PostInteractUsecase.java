package com.pbl.star.usecase;

public interface PostInteractUsecase {
    void likePost(String postId);
    void unlikePost(String postId);
    void repostPost(String postId);
    void unRepostPost(String postId);
    void reportPost(String postId);
}
