package com.pbl.star.services.domain;

import com.pbl.star.dtos.request.post.CreateReportParams;
import com.pbl.star.dtos.request.post.RejectPostParams;
import com.pbl.star.dtos.response.post.PostInteractionListResponse;
import com.pbl.star.enums.InteractType;
import com.pbl.star.enums.PostStatus;
import com.pbl.star.models.entities.Post;
import com.pbl.star.models.entities.PostLike;
import com.pbl.star.models.entities.PostReport;
import com.pbl.star.models.entities.PostRepost;

import java.time.Instant;

public interface PostInteractionService {
    PostLike likePost(String userId, String postId);
    void unlikePost(String userId, String postId);
    PostRepost repostPost(String userId, String postId);
    void deleteRepostPost(String userId, String postId);
    PostReport reportPost(String userId, String postId, CreateReportParams params);
    Post moderatePostStatus(String postId, PostStatus status, String moderatorId);
    Post rejectPost(String postId, String moderatorId, RejectPostParams params);
    void unmoderatePostStatus(String postId, String moderatorId);
    PostInteractionListResponse getActorProfilesOfPost(String currentUserId, String postId, InteractType type, int limit, Instant after);
}
