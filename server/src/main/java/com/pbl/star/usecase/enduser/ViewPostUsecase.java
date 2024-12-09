package com.pbl.star.usecase.enduser;

import com.pbl.star.dtos.query.post.PendingPostForUserDTO;
import com.pbl.star.dtos.query.post.PostForUserDTO;
import com.pbl.star.dtos.query.post.ReplyOnWallDTO;
import com.pbl.star.dtos.query.post.RepostOnWallDTO;
import com.pbl.star.dtos.response.CustomSlice;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface ViewPostUsecase {
    PostForUserDTO getPostById(String postId);
    Slice<PostForUserDTO> getPostsOnNewsfeed(int limit, Instant after);
    Slice<PostForUserDTO> getPostsOfFollowingUsers(int limit, Instant after);
    Slice<PostForUserDTO> getPostsOnUserWall(String userId, int limit, Instant after);
    Slice<PostForUserDTO> getPostsInRoomAsUser(String roomId, int limit, Instant after);
    CustomSlice<PostForUserDTO> getRepliesOfPost(String postId, int limit, Instant after);
    Slice<ReplyOnWallDTO> getRepliesOnUserWall(String userId, int limit, Instant after);
    Slice<RepostOnWallDTO> getRepostsOnUserWall(String userId, int limit, Instant after);
    Slice<PendingPostForUserDTO> getMyPendingPosts(int limit, Instant after);
}
