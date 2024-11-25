package com.pbl.star.services.domain;

import com.pbl.star.entities.PostLike;

public interface PostInteractionService {
    PostLike likePost(String userId, String postId);
    void unlikePost(String userId, String postId);
}
