package com.pbl.star.usecase.impl;

import com.pbl.star.services.domain.PostService;
import com.pbl.star.usecase.PostInteractUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostInteractUsecaseImpl implements PostInteractUsecase {
    private final PostService postService;

    @Override
    public void likePost(String postId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        postService.likePost(currentUserId, postId);
    }

    @Override
    public void unlikePost(String postId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        postService.unlikePost(currentUserId, postId);
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
