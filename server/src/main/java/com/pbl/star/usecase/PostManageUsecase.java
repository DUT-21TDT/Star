package com.pbl.star.usecase;

import com.pbl.star.dtos.query.post.PendingPostForUserDTO;
import com.pbl.star.dtos.query.post.PostForModDTO;
import com.pbl.star.dtos.query.post.PostForUserDTO;
import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.enums.PostStatus;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface PostManageUsecase {
    String createPost(CreatePostParams createPostParams);
    Slice<PendingPostForUserDTO> getPendingPostsByCurrentUser(int limit, Instant after);
    Slice<PostForUserDTO> getPostsOnUserWall(String userId, int limit, Instant after);
    Slice<PostForUserDTO> getPostsOnNewsfeed(int limit, Instant after);
    Slice<PostForUserDTO> getPostsInRoomAsUser(String roomId, int limit, Instant after);
    Slice<PostForModDTO> getPostsInRoomAsMod(String roomId, PostStatus status, int limit, Instant after);
    void approvePost(String postId);
    void rejectPost(String postId);
    void returnPostToPending(String postId);
}
