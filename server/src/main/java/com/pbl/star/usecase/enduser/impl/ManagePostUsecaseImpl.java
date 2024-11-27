package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.dtos.query.post.PendingPostForUserDTO;
import com.pbl.star.dtos.query.post.PostForUserDTO;
import com.pbl.star.dtos.query.post.ReplyOnWallDTO;
import com.pbl.star.dtos.query.post.RepostOnWallDTO;
import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.entities.Post;
import com.pbl.star.enums.PostStatus;
import com.pbl.star.services.domain.PostService;
import com.pbl.star.services.external.NotificationProducer;
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

    private final NotificationProducer notificationProducer;

    @Override
    public String createPost(CreatePostParams createPostParams) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        Post savedPost = postService.createPost(currentUserId, createPostParams);

        notificationProducer.pushNewPendingPostMessage(savedPost);

        return savedPost.getId();
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
        Post deletedPost = postService.deletePostOfUser(postId, currentUserId);

        if (deletedPost.getParentPostId() != null) {
            notificationProducer.pushDeleteReplyMessage(deletedPost.getParentPostId(), currentUserId);
        } else if (deletedPost.getStatus() == PostStatus.PENDING) {
            notificationProducer.pushRemovePendingPostMessage(deletedPost);
        }
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
}
