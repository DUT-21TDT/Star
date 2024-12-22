package com.pbl.star.services.domain;

import com.pbl.star.enums.NotificationType;
import com.pbl.star.models.entities.Notification;
import com.pbl.star.models.projections.notification.NotificationForUser;
import org.springframework.data.domain.Slice;

import java.time.Instant;
import java.util.List;

public interface NotificationService {
    Slice<NotificationForUser> getNotifications(String userId, int limit, Instant after);
    NotificationForUser getPushedNotification(String notificationObjId);

    /**
     * Create notification records for interact post.
     *
     * @param postId    the post id that is interacted
     * @param actorId   the actor id who interacted the post
     * @param timestamp the timestamp of the interaction
     * @param type      the type of the interaction
     */
    Notification createInteractPostNotification(String postId, String actorId, Instant timestamp, NotificationType type);
    void undoInteractPostNotification(String postId, String actorId, NotificationType type);

    Notification createFollowUserNotification(String followingId, String followerId, String followeeId, Instant timestamp, NotificationType notificationType);
    void undoFollowUserNotification(String followingId, NotificationType notificationType);
    Notification createModeratePostNotification(String postId, Instant timestamp, NotificationType notificationType);

    List<Notification> createNewPostNotification(String roomId, String actorId, Instant timestamp);
    void undoNewPostNotification(String roomId, String actorId);
}
