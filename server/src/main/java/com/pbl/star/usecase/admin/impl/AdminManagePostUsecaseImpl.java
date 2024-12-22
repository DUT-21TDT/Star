package com.pbl.star.usecase.admin.impl;

import com.pbl.star.dtos.request.post.FilterPostParams;
import com.pbl.star.dtos.response.post.PostForAdminResponse;
import com.pbl.star.mapper.post.PostDTOMapper;
import com.pbl.star.services.domain.PostService;
import com.pbl.star.usecase.admin.AdminManagePostUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminManagePostUsecaseImpl implements AdminManagePostUsecase {

    private final PostService postService;

    private final PostDTOMapper postMapper;

    @Override
    public Page<PostForAdminResponse> getAllPosts(int page, int size, FilterPostParams filter) {
        return postService.getPostsAsAdmin(page, size, filter)
                .map(postMapper::toDTO);
    }

    @Override
    public void hidePost(String postId) {
        postService.updateHideStatus(postId, true);
    }

    @Override
    public void unhidePost(String postId) {
        postService.updateHideStatus(postId, false);
    }
}
