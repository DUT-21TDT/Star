package com.pbl.star.usecase.impl;

import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.services.PostService;
import com.pbl.star.usecase.PostManageUsecase;
import com.pbl.star.utils.AuthUtil;
import com.pbl.star.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostManageUsecaseImpl implements PostManageUsecase {
    private final PostService postService;

    @Override
    public String createPost(CreatePostParams createPostParams) {
        CurrentUser currentUser = AuthUtil.getCurrentUser();
        return postService.createPost(currentUser.getId(), createPostParams);
    }
}
