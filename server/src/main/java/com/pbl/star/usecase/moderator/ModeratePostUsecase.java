package com.pbl.star.usecase.moderator;

import com.pbl.star.dtos.query.post.PostForModDTO;
import com.pbl.star.enums.PostStatus;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface ModeratePostUsecase {
    Slice<PostForModDTO> getPostsInRoomAsMod(String roomId, PostStatus status, int limit, Instant after);
    void approvePost(String postId);
    void rejectPost(String postId);
    void returnPostToPending(String postId);
}
