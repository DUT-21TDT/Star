package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.entities.Post;
import com.pbl.star.enums.PostStatus;
import com.pbl.star.services.domain.PostService;
import com.pbl.star.services.external.NotificationProducer;
import com.pbl.star.usecase.enduser.ManagePostUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
    public void deletePost(String postId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        Post deletedPost = postService.deletePostOfUser(postId, currentUserId);

        if (deletedPost.getParentPostId() != null) {
            notificationProducer.pushDeleteReplyMessage(deletedPost.getParentPostId(), currentUserId);
        } else if (deletedPost.getStatus() == PostStatus.PENDING) {
            notificationProducer.pushRemovePendingPostMessage(deletedPost);
        }
    }
}
