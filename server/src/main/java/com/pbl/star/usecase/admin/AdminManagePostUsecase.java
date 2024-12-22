package com.pbl.star.usecase.admin;

import com.pbl.star.dtos.request.post.FilterPostParams;
import com.pbl.star.dtos.response.post.PostForAdminResponse;
import org.springframework.data.domain.Page;

public interface AdminManagePostUsecase {
    Page<PostForAdminResponse> getAllPosts(int page, int size, FilterPostParams filter);
}
