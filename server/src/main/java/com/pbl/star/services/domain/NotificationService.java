package com.pbl.star.services.domain;

import com.pbl.star.dtos.query.notification.NotificationForUserDTO;
import com.pbl.star.enums.NotificationType;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface NotificationService {
    Slice<NotificationForUserDTO> getNotifications(String userId, int limit, Instant after);

    /**
     * Create notification records for interact post.
     *
     * @param postId    the post id that is interacted
     * @param actorId   the actor id who interacted the post
     * @param timestamp the timestamp of the interaction
     * @param type      the type of the interaction
     */
    void createInteractPostNotification(String postId, String actorId, Instant timestamp, NotificationType type);
    void undoInteractPostNotification(String postId, String actorId, NotificationType type);

    void createFollowUserNotification(String followingId, String followerId, String followeeId, Instant timestamp, NotificationType notificationType);
    void undoFollowUserNotification(String followingId, NotificationType notificationType);
    void createModeratePostNotification(String postId, Instant timestamp, NotificationType notificationType);

    void createNewPostNotification(String roomId, String actorId, Instant timestamp);
    void undoNewPostNotification(String roomId, String actorId);
}
