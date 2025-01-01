package com.pbl.star.usecase.moderator;

import com.pbl.star.dtos.request.post.RejectPostParams;
import com.pbl.star.dtos.response.post.PostForModResponse;
import com.pbl.star.dtos.response.post.ReportForModResponse;
import com.pbl.star.enums.PostStatus;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface ModeratePostUsecase {
    Slice<PostForModResponse> getPostsInRoomAsMod(String roomId, PostStatus status, int limit, Instant after);
    void approvePost(String postId);
    void rejectPost(String postId, RejectPostParams params);
    void returnPostToPending(String postId);
    Slice<ReportForModResponse> getReportsOfPost(String postId, int limit, Instant after);
}
