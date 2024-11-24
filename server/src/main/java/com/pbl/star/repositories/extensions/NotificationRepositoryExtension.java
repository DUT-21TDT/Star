package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.notification.NotificationForUserDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface NotificationRepositoryExtension {
    Slice<NotificationForUserDTO> getNotifications(Pageable pageable, Instant after, String userId);
}
