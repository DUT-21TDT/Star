package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.dtos.query.post.PendingPostForUserDTO;
import com.pbl.star.dtos.query.post.PostForUserDTO;
import com.pbl.star.dtos.query.post.ReplyOnWallDTO;
import com.pbl.star.dtos.query.post.RepostOnWallDTO;
import com.pbl.star.dtos.response.CustomSlice;
import com.pbl.star.enums.PostStatus;
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

    @Override
    public PostForUserDTO getPostById(String postId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.getPostById(currentUserId, postId);
    }

    @Override
    public Slice<PostForUserDTO> getPostsOnNewsfeed(int limit, Instant after) {
        String userId = AuthUtil.getCurrentUser().getId();
        return postService.getPostsOnNewsfeed(userId, limit, after);
    }

    @Override
    public Slice<PostForUserDTO> getPostsOfFollowingUsers(int limit, Instant after) {
        String userId = AuthUtil.getCurrentUser().getId();
        return postService.getPostsOfFollowingUsers(userId, limit, after);
    }

    @Override
    public Slice<PostForUserDTO> getPostsOnUserWall(String userId, int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.getPostsOnUserWall(currentUserId, userId, limit, after);
    }

    @Override
    public Slice<PostForUserDTO> getPostsInRoomAsUser(String roomId, int limit, Instant after) {
        return postService.getPostsInRoom(roomId, PostStatus.APPROVED, limit, after);
    }

    @Override
    public CustomSlice<PostForUserDTO> getRepliesOfPost(String postId, int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.getReplies(currentUserId, postId, limit, after);
    }

    @Override
    public Slice<ReplyOnWallDTO> getRepliesOnUserWall(String userId, int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.getRepliesOnWall(currentUserId, userId, limit, after);
    }

    @Override
    public Slice<RepostOnWallDTO> getRepostsOnUserWall(String userId, int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.getRepostsOnWall(currentUserId, userId, limit, after);
    }

    @Override
    public Slice<PendingPostForUserDTO> getMyPendingPosts(int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.getPendingPostsByUser(currentUserId, limit, after);
    }
}
