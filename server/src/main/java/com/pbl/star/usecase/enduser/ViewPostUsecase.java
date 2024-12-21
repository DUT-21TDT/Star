package com.pbl.star.usecase.enduser;

import com.pbl.star.dtos.response.post.*;
import com.pbl.star.models.projections.post.PendingPostForUser;
import com.pbl.star.models.projections.post.ReplyOnWall;
import com.pbl.star.models.projections.post.RepostOnWall;
import com.pbl.star.dtos.response.CustomSlice;
import com.pbl.star.enums.InteractType;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface ViewPostUsecase {
    PostForUserResponse getPostById(String postId);
    Slice<PostForUserResponse> getPostsOnNewsfeed(int limit, Instant after);
    Slice<PostForUserResponse> getPostsOfFollowingUsers(int limit, Instant after);
    Slice<PostForUserResponse> getPostsOnUserWall(String userId, int limit, Instant after);
    Slice<PostForUserResponse> getPostsInRoomAsUser(String roomId, int limit, Instant after);
    CustomSlice<PostForUserResponse> getRepliesOfPost(String postId, int limit, Instant after);
    PostInteractionListResponse getActorProfilesOfPost(String postId, InteractType type, int limit, Instant after);
    Slice<ReplyOnWallResponse> getRepliesOnUserWall(String userId, int limit, Instant after);
    Slice<RepostOnWallResponse> getRepostsOnUserWall(String userId, int limit, Instant after);
    Slice<PendingPostForUserResponse> getMyPendingPosts(int limit, Instant after);
}
