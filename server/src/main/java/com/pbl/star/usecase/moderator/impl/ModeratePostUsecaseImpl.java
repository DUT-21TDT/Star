package com.pbl.star.usecase.moderator.impl;

import com.pbl.star.dtos.response.post.PostForModResponse;
import com.pbl.star.mapper.post.PostDTOMapper;
import com.pbl.star.models.entities.Post;
import com.pbl.star.enums.PostStatus;
import com.pbl.star.exceptions.ModeratorAccessException;
import com.pbl.star.services.domain.PostInteractionService;
import com.pbl.star.services.domain.PostService;
import com.pbl.star.services.domain.UserRoomService;
import com.pbl.star.services.external.NotificationProducer;
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
    private final PostInteractionService postInteractionService;
    private final UserRoomService userRoomService;

    private final NotificationProducer notificationProducer;

    private final PostDTOMapper postMapper;

    @Override
    public Slice<PostForModResponse> getPostsInRoomAsMod(String roomId, PostStatus status, int limit, Instant after) {
        String userId = AuthUtil.getCurrentUser().getId();
        if (!userRoomService.isModeratorOfRoom(userId, roomId)) {
            throw new ModeratorAccessException("User is not a moderator of the room");
        }

        return postService.getPostsInRoomAsMod(roomId, status, limit, after)
                .map(postMapper::toDTO);
    }

    @Override
    public void approvePost(String postId) {
        String moderatorId = AuthUtil.getCurrentUser().getId();
        Post approvedPost = postInteractionService.moderatePostStatus(postId, PostStatus.APPROVED, moderatorId);

        notificationProducer.pushApprovePostMessage(approvedPost);
    }

    @Override
    public void rejectPost(String postId) {
        String moderatorId = AuthUtil.getCurrentUser().getId();
        Post rejectedPost = postInteractionService.moderatePostStatus(postId, PostStatus.REJECTED, moderatorId);

        notificationProducer.pushRejectPostMessage(rejectedPost);
    }

    @Override
    public void returnPostToPending(String postId) {
        String moderatorId = AuthUtil.getCurrentUser().getId();
        postInteractionService.unmoderatePostStatus(postId, moderatorId);
    }
}
