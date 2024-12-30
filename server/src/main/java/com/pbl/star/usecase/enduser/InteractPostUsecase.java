package com.pbl.star.usecase.enduser;

import com.pbl.star.dtos.request.post.CreateReplyParams;
import com.pbl.star.dtos.request.post.CreateReportParams;


public interface InteractPostUsecase {
    void likePost(String postId);
    void unlikePost(String postId);
    String replyPost(String postId, CreateReplyParams createReplyParams);
    void repostPost(String postId);
    void deleteRepostPost(String postId);
    void reportPost(String postId, CreateReportParams params);
}
