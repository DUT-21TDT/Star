package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.entities.Post;
import com.pbl.star.entities.PostLike;
import com.pbl.star.entities.PostRepost;
import com.pbl.star.services.domain.PostInteractionService;
import com.pbl.star.services.domain.PostService;
import com.pbl.star.services.external.NotificationProducer;
import com.pbl.star.usecase.enduser.InteractPostUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


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

        notificationProducer.pushLikePostMessage(like);
    }

    @Override
    public void unlikePost(String postId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        postInteractionService.unlikePost(currentUserId, postId);

        notificationProducer.pushUnlikePostMessage(postId, currentUserId);
    }

    @Override
    public String replyPost(CreatePostParams createReplyParams) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        Post savedReply = postService.createReply(currentUserId, createReplyParams);

        notificationProducer.pushReplyPostMessage(savedReply);

        return savedReply.getId();
    }

    @Override
    public void repostPost(String postId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        PostRepost postRepost = postInteractionService.repostPost(currentUserId, postId);

        notificationProducer.pushRepostPostMessage(postRepost);
    }

    @Override
    public void deleteRepostPost(String postId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        postInteractionService.deleteRepostPost(currentUserId, postId);

        notificationProducer.pushDeleteRepostPostMessage(postId, currentUserId);
    }

    @Override
    public void reportPost(String postId) {

    }
}
