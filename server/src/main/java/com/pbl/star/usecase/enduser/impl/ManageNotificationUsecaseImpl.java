package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.dtos.response.notification.NotificationForUserResponse;
import com.pbl.star.mapper.notification.NotificationDTOMapper;
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

    private final NotificationDTOMapper notificationMapper;
    @Override
    public Slice<NotificationForUserResponse> getNotifications(int limit, Instant after) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return notificationService.getNotifications(currentUserId, limit, after)
                .map(notificationMapper::toDTO);
    }

    @Override
    public void markAsRead(String notificationId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        notificationService.markAsRead(currentUserId, notificationId);
    }
}
