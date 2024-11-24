package com.pbl.star.services.domain.impl;

import com.pbl.star.dtos.query.notification.NotificationForUserDTO;
import com.pbl.star.repositories.NotificationRepository;
import com.pbl.star.services.domain.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Slice<NotificationForUserDTO> getNotifications(String userId, int limit, Instant after) {
        Pageable pageable = PageRequest.of(0, limit);
        return notificationRepository.getNotifications(pageable, after, userId);
    }
}
