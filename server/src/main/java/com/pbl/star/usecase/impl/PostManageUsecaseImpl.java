package com.pbl.star.usecase.impl;

import com.pbl.star.dtos.query.post.PostForModDTO;
import com.pbl.star.dtos.query.post.PostForUserDTO;
import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.enums.PostStatus;
import com.pbl.star.exceptions.ModeratorAccessException;
import com.pbl.star.services.domain.PostService;
import com.pbl.star.services.domain.UserRoomService;
import com.pbl.star.usecase.PostManageUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class PostManageUsecaseImpl implements PostManageUsecase {
    private final PostService postService;
    private final UserRoomService userRoomService;

    @Override
    public String createPost(CreatePostParams createPostParams) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.createPost(currentUserId, createPostParams);
    }

    @Override
    public Slice<PostForUserDTO> getPostsOnUserWall(String userId, int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.getPostsOnUserWall(currentUserId, userId, limit, after);
    }

    @Override
    public Slice<PostForUserDTO> getPostsOnNewsfeed(int limit, Instant after) {
        String userId = AuthUtil.getCurrentUser().getId();
        return postService.getPostsOnNewsfeed(userId, limit, after);
    }

    @Override
    public Slice<PostForUserDTO> getApprovedPostsInRoom(String roomId, int limit, Instant after) {
        return postService.getPostsInRoom(roomId, PostStatus.APPROVED, limit, after);
    }

    @Override
    public Slice<PostForModDTO> getPostsInRoomAsMod(String roomId, PostStatus status, int limit, Instant after) {
        String userId = AuthUtil.getCurrentUser().getId();
        if (!userRoomService.isModeratorOfRoom(userId, roomId)) {
            throw new ModeratorAccessException("User is not a moderator of the room");
        }

        return postService.getPostsInRoomAsMod(roomId, status, limit, after);
    }
}
