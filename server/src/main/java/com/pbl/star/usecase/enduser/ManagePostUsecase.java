package com.pbl.star.usecase.enduser;

import com.pbl.star.dtos.query.post.PendingPostForUserDTO;
import com.pbl.star.dtos.query.post.PostForUserDTO;
import com.pbl.star.dtos.query.post.ReplyOnWallDTO;
import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.entities.Post;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface ManagePostUsecase {
    String createPost(CreatePostParams createPostParams);
    PostForUserDTO getPostById(String postId);
    Slice<PendingPostForUserDTO> getMyPendingPosts(int limit, Instant after);
    Slice<PostForUserDTO> getPostsOnUserWall(String userId, int limit, Instant after);
    Slice<PostForUserDTO> getPostsOnNewsfeed(int limit, Instant after);
    Slice<PostForUserDTO> getPostsInRoomAsUser(String roomId, int limit, Instant after);
    void deletePost(String postId);
    Slice<ReplyOnWallDTO> getRepliesOnUserWall(String userId, int limit, Instant after);
}
