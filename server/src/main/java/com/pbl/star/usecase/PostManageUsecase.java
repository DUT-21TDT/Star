package com.pbl.star.usecase;

import com.pbl.star.dtos.query.post.PostForModDTO;
import com.pbl.star.dtos.query.post.PostForUserDTO;
import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.enums.PostStatus;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface PostManageUsecase {
    String createPost(CreatePostParams createPostParams);
    Slice<PostForUserDTO> getPostsOnUserWall(String userId, int limit, Instant after);
    Slice<PostForUserDTO> getPostsOnNewsfeed(int limit, Instant after);
    Slice<PostForUserDTO> getApprovedPostsInRoom(String roomId, int limit, Instant after);
    Slice<PostForModDTO> getPostsInRoomAsMod(String roomId, PostStatus status, int limit, Instant after);
}
