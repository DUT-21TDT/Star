package com.pbl.star.services.domain.impl;

import com.pbl.star.dtos.query.notification.NotificationForUserDTO;
import com.pbl.star.entities.Notification;
import com.pbl.star.entities.NotificationChange;
import com.pbl.star.entities.NotificationObject;
import com.pbl.star.enums.NotificationType;
import com.pbl.star.repositories.NotificationChangeRepository;
import com.pbl.star.repositories.NotificationObjectRepository;
import com.pbl.star.repositories.NotificationRepository;
import com.pbl.star.services.domain.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationObjectRepository notificationObjectRepository;
    private final NotificationChangeRepository notificationChangeRepository;

    @Override
    public Slice<NotificationForUserDTO> getNotifications(String userId, int limit, Instant after) {
        Pageable pageable = PageRequest.of(0, limit);
        return notificationRepository.getNotifications(pageable, after, userId);
    }

    @Override
    @Transactional
    public void createLikePostNotification(String postId, String actorId, Instant timestamp, String receiverId) {

        boolean isExistNotification = true;

        // Upsert notification object
        Optional<NotificationObject> notificationObjOpt = notificationObjectRepository.findByNotificationTypeAndArtifactId(NotificationType.LIKE_POST, postId);
        NotificationObject savedNotificationObj;

        if (notificationObjOpt.isPresent()) {
            NotificationObject notificationObj = notificationObjOpt.get();
            notificationObj.setRead(false);
            savedNotificationObj = notificationObjectRepository.save(notificationObj);
        } else {
            NotificationObject obj = NotificationObject.builder()
                    .notificationType(NotificationType.LIKE_POST)
                    .artifactId(postId)
                    .isRead(false)
                    .build();
            try {
                isExistNotification = false;
                savedNotificationObj = notificationObjectRepository.save(obj);
            } catch (DataIntegrityViolationException e) {
                NotificationObject existedObj = notificationObjectRepository.findByNotificationTypeAndArtifactId(NotificationType.LIKE_POST, postId)
                        .orElseThrow(() -> new RuntimeException("Failed to resolve race condition"));
                existedObj.setRead(false);

                isExistNotification = true;
                savedNotificationObj = notificationObjectRepository.save(existedObj);
            }
        }

        // Always create a new notification change
        NotificationChange notificationChange = NotificationChange.builder()
                .notificationObjectId(savedNotificationObj.getId())
                .actorId(actorId)
                .changeAt(timestamp)
                .build();
        notificationChangeRepository.save(notificationChange);

        if (!isExistNotification) {
            Notification notification = Notification.builder()
                    .notificationObjectId(savedNotificationObj.getId())
                    .receiverId(receiverId)
                    .build();
            notificationRepository.save(notification);
        }
    }
}
