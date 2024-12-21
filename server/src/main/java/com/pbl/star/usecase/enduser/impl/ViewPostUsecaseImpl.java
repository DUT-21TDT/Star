package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.dtos.response.post.*;
import com.pbl.star.mapper.post.PostDTOMapper;
import com.pbl.star.dtos.response.CustomSlice;
import com.pbl.star.enums.InteractType;
import com.pbl.star.enums.PostStatus;
import com.pbl.star.services.domain.PostInteractionService;
import com.pbl.star.services.domain.PostService;
import com.pbl.star.usecase.enduser.ViewPostUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ViewPostUsecaseImpl implements ViewPostUsecase {

    private final PostService postService;
    private final PostInteractionService postInteractionService;

    private final PostDTOMapper postMapper;

    @Override
    public PostForUserResponse getPostById(String postId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postMapper.toDTO(postService.getPostById(currentUserId, postId));
    }

    @Override
    public Slice<PostForUserResponse> getPostsOnNewsfeed(int limit, Instant after) {
        String userId = AuthUtil.getCurrentUser().getId();
        return postService.getPostsOnNewsfeed(userId, limit, after)
                .map(postMapper::toDTO);
    }

    @Override
    public Slice<PostForUserResponse> getPostsOfFollowingUsers(int limit, Instant after) {
        String userId = AuthUtil.getCurrentUser().getId();
        return postService.getPostsOfFollowingUsers(userId, limit, after)
                .map(postMapper::toDTO);
    }

    @Override
    public Slice<PostForUserResponse> getPostsOnUserWall(String userId, int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.getPostsOnUserWall(currentUserId, userId, limit, after)
                .map(postMapper::toDTO);
    }

    @Override
    public Slice<PostForUserResponse> getPostsInRoomAsUser(String roomId, int limit, Instant after) {
        return postService.getPostsInRoom(roomId, PostStatus.APPROVED, limit, after)
                .map(postMapper::toDTO);
    }

    @Override
    public CustomSlice<PostForUserResponse> getRepliesOfPost(String postId, int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.getReplies(currentUserId, postId, limit, after)
                .map(postMapper::toDTO);
    }

    @Override
    public PostInteractionListResponse getActorProfilesOfPost(String postId, InteractType type, int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postInteractionService.getActorProfilesOfPost(currentUserId, postId, type, limit, after);
    }

    @Override
    public Slice<ReplyOnWallResponse> getRepliesOnUserWall(String userId, int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.getRepliesOnWall(currentUserId, userId, limit, after)
                .map(postMapper::toDTO);
    }

    @Override
    public Slice<RepostOnWallResponse> getRepostsOnUserWall(String userId, int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.getRepostsOnWall(currentUserId, userId, limit, after)
                .map(postMapper::toDTO);
    }

    @Override
    public Slice<PendingPostForUserResponse> getMyPendingPosts(int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.getPendingPostsByUser(currentUserId, limit, after)
                .map(postMapper::toDTO);
    }
}
