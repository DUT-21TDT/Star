package com.pbl.star.services.domain;

import com.pbl.star.dtos.query.notification.NotificationForUserDTO;
import org.springframework.data.domain.Slice;

import java.time.Instant;
import java.util.List;

public interface NotificationService {
    Slice<NotificationForUserDTO> getNotifications(String userId, int limit, Instant after);

    void createLikePostNotification(String postId, String actorId, Instant timestamp, String receiverId);
    void undoLikePostNotification(String postId, String actorId);

    void createReplyPostNotification(String postId, String actorId, Instant timestamp, String receiverId);
    void undoReplyPostNotification(String parentPostId, String actorId);

    void createRepostPostNotification(String postId, String actorId, Instant timestamp, String receiverId);
    void undoRepostPostNotification(String postId, String actorId);

    void createFollowNotification(String followingId, String followerId, String followeeId, Instant timestamp);
    void undoFollowNotification(String followingId);

    void createRequestFollowNotification(String followingId, String followerId, String followeeId, Instant timestamp);
    void undoRequestFollowNotification(String followingId);

    void createAcceptFollowNotification(String followingId, String followerId, String followeeId, Instant timestamp);

    void createApprovePostNotification(String postId, Instant timestamp, String receiverId);

    void createRejectPostNotification(String postId, Instant timestamp, String receiverId);

    void createNewPostNotification(String roomId, String actorId, Instant timestamp, List<String> receiverId);
    void undoNewPostNotification(String roomId, String actorId);
}
