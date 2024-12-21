package com.pbl.star.usecase.moderator;

import com.pbl.star.dtos.response.post.PostForModResponse;
import com.pbl.star.models.projections.post.PostForMod;
import com.pbl.star.enums.PostStatus;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface ModeratePostUsecase {
    Slice<PostForModResponse> getPostsInRoomAsMod(String roomId, PostStatus status, int limit, Instant after);
    void approvePost(String postId);
    void rejectPost(String postId);
    void returnPostToPending(String postId);
}
