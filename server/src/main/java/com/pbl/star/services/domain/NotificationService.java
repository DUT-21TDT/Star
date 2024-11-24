package com.pbl.star.services.domain;

import com.pbl.star.dtos.query.notification.NotificationForUserDTO;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface NotificationService {
    Slice<NotificationForUserDTO> getNotifications(String userId, int limit, Instant after);
}
