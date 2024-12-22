package com.pbl.star.services.domain;

import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.dtos.request.post.CreateReplyParams;
import com.pbl.star.dtos.request.post.FilterPostParams;
import com.pbl.star.dtos.response.CustomSlice;
import com.pbl.star.enums.PostStatus;
import com.pbl.star.models.entities.Post;
import com.pbl.star.models.projections.post.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface PostService {
    Post createPost(String userId, CreatePostParams createPostParams);
    PostForUser getPostById(String currentUserId, String postId);
    Page<PostForAdmin> getPostsAsAdmin(int page, int size, FilterPostParams filter);
    Slice<PostForUser> getPostsOnUserWall(String currentUserId, String targetUserId, int limit, Instant after);
    Slice<PendingPostForUser> getPendingPostsByUser(String userId, int limit, Instant after);
    Slice<PostForUser> getPostsOnNewsfeed(String currentUserId, int limit, Instant after);
    Slice<PostForUser> getPostsOfFollowingUsers(String currentUserId, int limit, Instant after);
    Slice<PostForUser> getPostsInRoom(String roomId, PostStatus status, int limit, Instant after);
    Slice<PostForMod> getPostsInRoomAsMod(String roomId, PostStatus status, int limit, Instant after);
    Post deletePostOfUser(String postId, String userId);
    Post createReply(String userId, String postId, CreateReplyParams createReplyParams);
    CustomSlice<PostForUser> getReplies(String userId, String postId, int limit, Instant after);
    Slice<ReplyOnWall> getRepliesOnWall(String currentUserId, String targetUserId, int limit, Instant after);
    Slice<RepostOnWall> getRepostsOnWall(String currentUserId, String targetUserId, int limit, Instant after);
    void updateHideStatus(String postId, boolean hide);
}
