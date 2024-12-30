package com.pbl.star.services.external;

import com.pbl.star.models.entities.*;

public interface NotificationProducer {
    void pushLikePostMessage(PostLike postLike);
    void pushUnlikePostMessage(String postId, String actorId);

    void pushReplyPostMessage(Post reply);
    void pushDeleteReplyMessage(String replyId, String actorId);

    void pushRepostPostMessage(PostRepost postRepost);
    void pushDeleteRepostPostMessage(String postId, String actorId);

    void pushReportPostMessage(PostReport postReport);

    void pushFollowMessage(Following following);
    void pushUnfollowMessage(Following following);

    void pushRequestFollowMessage(Following following);
    void pushRevokeRequestFollowMessage(Following following);

    void pushAcceptFollowMessage(Following following);

    void pushApprovePostMessage(Post post);
    void pushRejectPostMessage(Post post);

    void pushNewPendingPostMessage(Post post);
    void pushRemovePendingPostMessage(Post post);
}
