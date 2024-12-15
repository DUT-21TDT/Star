package com.pbl.star.services.domain;

import com.pbl.star.dtos.query.user.OnInteractProfile;
import com.pbl.star.dtos.response.CustomSlice;
import com.pbl.star.dtos.response.post.PostInteractionList;
import com.pbl.star.entities.Post;
import com.pbl.star.entities.PostLike;
import com.pbl.star.entities.PostRepost;
import com.pbl.star.enums.InteractType;
import com.pbl.star.enums.PostStatus;

import java.time.Instant;

public interface PostInteractionService {
    PostLike likePost(String userId, String postId);
    void unlikePost(String userId, String postId);
    PostRepost repostPost(String userId, String postId);
    void deleteRepostPost(String userId, String postId);
    Post moderatePostStatus(String postId, PostStatus status, String moderatorId);
    void unmoderatePostStatus(String postId, String moderatorId);
    PostInteractionList getActorProfilesOfPost(String currentUserId, String postId, InteractType type, int limit, Instant after);
}
