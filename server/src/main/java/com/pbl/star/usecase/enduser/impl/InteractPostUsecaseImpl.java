package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.services.domain.PostInteractionService;
import com.pbl.star.usecase.enduser.InteractPostUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InteractPostUsecaseImpl implements InteractPostUsecase {

    private final PostInteractionService postInteractionService;

    @Override
    public void likePost(String postId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        postInteractionService.likePost(currentUserId, postId);
    }

    @Override
    public void unlikePost(String postId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        postInteractionService.unlikePost(currentUserId, postId);
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
