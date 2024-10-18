package com.pbl.star.usecase.impl;

import com.pbl.star.dtos.query.post.PostOverviewDTO;
import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.services.domain.PostService;
import com.pbl.star.usecase.PostManageUsecase;
import com.pbl.star.utils.AuthUtil;
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
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return postService.createPost(currentUserId, createPostParams);
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
