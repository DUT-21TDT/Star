package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.dtos.query.post.PendingPostForUserDTO;
import com.pbl.star.dtos.query.post.PostForUserDTO;
import com.pbl.star.dtos.query.post.ReplyOnWallDTO;
import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.enums.PostStatus;
import com.pbl.star.services.domain.PostService;
import com.pbl.star.usecase.enduser.ManagePostUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ManagePostUsecaseImpl implements ManagePostUsecase {
    private final PostService postService;

    @Override
    public String createPost(CreatePostParams createPostParams) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.createPost(currentUserId, createPostParams);
    }

    @Override
    public PostForUserDTO getPostById(String postId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.getPostById(currentUserId, postId);
    }

    @Override
    public Slice<PendingPostForUserDTO> getMyPendingPosts(int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.getPendingPostsByUser(currentUserId, limit, after);
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
    public Slice<PostForUserDTO> getPostsInRoomAsUser(String roomId, int limit, Instant after) {
        return postService.getPostsInRoom(roomId, PostStatus.APPROVED, limit, after);
    }

    @Override
    public void deletePost(String postId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        postService.deletePostOfUser(postId, currentUserId);
    }

    @Override
    public Slice<ReplyOnWallDTO> getRepliesOnUserWall(String userId, int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.getRepliesOnWall(currentUserId, userId, limit, after);
    }
}
