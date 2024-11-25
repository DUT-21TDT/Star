package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.dtos.query.post.PostForUserDTO;
import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.dtos.response.CustomSlice;
import com.pbl.star.entities.PostLike;
import com.pbl.star.services.domain.PostInteractionService;
import com.pbl.star.services.domain.PostService;
import com.pbl.star.services.external.NotificationProducer;
import com.pbl.star.usecase.enduser.InteractPostUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class InteractPostUsecaseImpl implements InteractPostUsecase {

    private final PostService postService;
    private final PostInteractionService postInteractionService;
    private final NotificationProducer notificationProducer;


    @Override
    public void likePost(String postId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        PostLike like = postInteractionService.likePost(currentUserId, postId);

        notificationProducer.pushLikePostEvent(like);
    }

    @Override
    public void unlikePost(String postId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        postInteractionService.unlikePost(currentUserId, postId);
    }

    @Override
    public String replyPost(CreatePostParams createReplyParams) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.createReply(currentUserId, createReplyParams);
    }

    @Override
    public CustomSlice<PostForUserDTO> getRepliesOfPost(String postId, int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.getReplies(currentUserId, postId, limit, after);
    }

    @Override
    public void repostPost(String postId) {

    }

    @Override
    public void unRepostPost(String postId) {

    }

    @Override
    public void reportPost(String postId) {

    }
}
