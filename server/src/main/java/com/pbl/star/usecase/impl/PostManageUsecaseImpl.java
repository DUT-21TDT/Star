package com.pbl.star.usecase.impl;

import com.pbl.star.dtos.query.post.PostOverviewDTO;
import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.services.PostService;
import com.pbl.star.usecase.PostManageUsecase;
import com.pbl.star.utils.AuthUtil;
import com.pbl.star.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class PostManageUsecaseImpl implements PostManageUsecase {
    private final PostService postService;

    @Override
    public String createPost(CreatePostParams createPostParams) {
        CurrentUser currentUser = AuthUtil.getCurrentUser();
        return postService.createPost(currentUser.getId(), createPostParams);
    }

    @Override
    public Slice<PostOverviewDTO> getPostsOnUserWall(String userId, int limit, Instant after) {
        return postService.getPostsOnUserWall(userId, limit, after);
    }

    @Override
    public Slice<PostOverviewDTO> getPostsOnNewsfeed(int limit, Instant after) {
        String userId = AuthUtil.getCurrentUser().getId();
        return postService.getPostsOnNewsfeed(userId, limit, after);
    }
}
