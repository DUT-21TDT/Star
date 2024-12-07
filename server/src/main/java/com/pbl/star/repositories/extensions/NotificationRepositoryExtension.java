package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.notification.NotificationForUserDTO;

import java.time.Instant;
import java.util.List;

public interface NotificationRepositoryExtension {
    List<NotificationForUserDTO> getNotifications(int limit, Instant after, String userId);
}
