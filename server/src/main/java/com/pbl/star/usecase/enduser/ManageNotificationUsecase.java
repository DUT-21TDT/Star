package com.pbl.star.usecase.enduser;

import com.pbl.star.dtos.query.notification.NotificationForUserDTO;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface ManageNotificationUsecase {
    Slice<NotificationForUserDTO> getNotifications(int limit, Instant after);
}
