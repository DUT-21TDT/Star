package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.dtos.query.notification.NotificationForUserDTO;
import com.pbl.star.services.domain.NotificationService;
import com.pbl.star.usecase.enduser.ManageNotificationUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ManageNotificationUsecaseImpl implements ManageNotificationUsecase {

    private final NotificationService notificationService;
    @Override
    public Slice<NotificationForUserDTO> getNotifications(int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return notificationService.getNotifications(currentUserId, limit, after);
    }
}
