package com.pbl.star.services.domain;

import com.pbl.star.entities.PostLike;
import com.pbl.star.entities.PostRepost;

public interface PostInteractionService {
    PostLike likePost(String userId, String postId);
    void unlikePost(String userId, String postId);
    PostRepost repostPost(String userId, String postId);
    void deleteRepostPost(String userId, String postId);
}
