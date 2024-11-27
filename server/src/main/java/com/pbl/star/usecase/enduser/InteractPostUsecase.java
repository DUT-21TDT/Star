package com.pbl.star.usecase.enduser;

import com.pbl.star.dtos.query.post.PostForUserDTO;
import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.dtos.response.CustomSlice;

import java.time.Instant;

public interface InteractPostUsecase {
    void likePost(String postId);
    void unlikePost(String postId);
    String replyPost(CreatePostParams createReplyParams);
    CustomSlice<PostForUserDTO> getRepliesOfPost(String postId, int limit, Instant after);
    void repostPost(String postId);
    void deleteRepostPost(String postId);
    void reportPost(String postId);
}
