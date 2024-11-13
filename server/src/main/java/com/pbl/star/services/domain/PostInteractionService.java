package com.pbl.star.services.domain;

public interface PostInteractionService {
    void likePost(String userId, String postId);
    void unlikePost(String userId, String postId);
}
