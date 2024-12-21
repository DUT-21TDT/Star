package com.pbl.star.repositories.extensions;

import com.pbl.star.models.projections.notification.NotificationForUser;

import java.time.Instant;
import java.util.List;

public interface NotificationRepositoryExtension {
    List<NotificationForUser> getNotifications(int limit, Instant after, String userId);
}
