package com.pbl.star.usecase;

import com.pbl.star.dtos.query.post.PostOverviewDTO;
import com.pbl.star.dtos.request.post.CreatePostParams;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface PostManageUsecase {
    String createPost(CreatePostParams createPostParams);
    Slice<PostOverviewDTO> getPostsByUser(String userId, int limit, Instant after);
}
