package com.pbl.star.usecase.moderator.impl;

import com.pbl.star.dtos.query.post.PostForModDTO;
import com.pbl.star.enums.PostStatus;
import com.pbl.star.exceptions.ModeratorAccessException;
import com.pbl.star.services.domain.PostService;
import com.pbl.star.services.domain.UserRoomService;
import com.pbl.star.usecase.moderator.ModeratePostUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ModeratePostUsecaseImpl implements ModeratePostUsecase {

    private final PostService postService;
    private final UserRoomService userRoomService;

    @Override
    public Slice<PostForModDTO> getPostsInRoomAsMod(String roomId, PostStatus status, int limit, Instant after) {
        String userId = AuthUtil.getCurrentUser().getId();
        if (!userRoomService.isModeratorOfRoom(userId, roomId)) {
            throw new ModeratorAccessException("User is not a moderator of the room");
        }

        return postService.getPostsInRoomAsMod(roomId, status, limit, after);
    }

    @Override
    public void approvePost(String postId) {
        String moderatorId = AuthUtil.getCurrentUser().getId();
        postService.moderatePostStatus(postId, PostStatus.APPROVED, moderatorId);
    }

    @Override
    public void rejectPost(String postId) {
        String moderatorId = AuthUtil.getCurrentUser().getId();
        postService.moderatePostStatus(postId, PostStatus.REJECTED, moderatorId);
    }

    @Override
    public void returnPostToPending(String postId) {
        String moderatorId = AuthUtil.getCurrentUser().getId();
        postService.unmoderatePostStatus(postId, moderatorId);
    }
}
