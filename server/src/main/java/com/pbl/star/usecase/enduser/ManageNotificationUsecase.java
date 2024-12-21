package com.pbl.star.usecase.enduser;

import com.pbl.star.dtos.response.notification.NotificationForUserResponse;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface ManageNotificationUsecase {
    Slice<NotificationForUserResponse> getNotifications(int limit, Instant after);
}
