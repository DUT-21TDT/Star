package com.pbl.star.services.domain.impl;

import com.pbl.star.dtos.query.notification.NotificationForUserDTO;
import com.pbl.star.entities.Notification;
import com.pbl.star.entities.NotificationChange;
import com.pbl.star.entities.NotificationObject;
import com.pbl.star.enums.ArtifactType;
import com.pbl.star.enums.NotificationType;
import com.pbl.star.repositories.NotificationChangeRepository;
import com.pbl.star.repositories.NotificationObjectRepository;
import com.pbl.star.repositories.NotificationRepository;
import com.pbl.star.services.domain.NotificationService;
import com.pbl.star.utils.SliceTransfer;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationObjectRepository notificationObjectRepository;
    private final NotificationChangeRepository notificationChangeRepository;

    @Override
    public Slice<NotificationForUserDTO> getNotifications(String userId, int limit, Instant after) {
        List<NotificationForUserDTO> notiList = notificationRepository.getNotifications(limit + 1, after, userId);
        return SliceTransfer.trimToSlice(notiList, limit);
    }

    @Override
    @Transactional
    public void createLikePostNotification(String postId, String actorId, Instant timestamp, String receiverId) {
        createInteractPostNotification(postId, actorId, timestamp, receiverId, NotificationType.LIKE_POST);
    }

    @Override
    @Transactional
    public void undoLikePostNotification(String postId, String actorId) {
        undoInteractPostNotification(postId, actorId, NotificationType.LIKE_POST);
    }

    @Override
    @Transactional
    public void createReplyPostNotification(String postId, String actorId, Instant timestamp, String receiverId) {
        createInteractPostNotification(postId, actorId, timestamp, receiverId, NotificationType.REPLY_POST);
    }

    @Override
    @Transactional
    public void undoReplyPostNotification(String parentPostId, String actorId) {
        undoInteractPostNotification(parentPostId, actorId, NotificationType.REPLY_POST);
    }

    @Override
    @Transactional
    public void createRepostPostNotification(String postId, String actorId, Instant timestamp, String receiverId) {
        createInteractPostNotification(postId, actorId, timestamp, receiverId, NotificationType.REPOST_POST);
    }

    @Override
    @Transactional
    public void undoRepostPostNotification(String postId, String actorId) {
        undoInteractPostNotification(postId, actorId, NotificationType.REPOST_POST);
    }

    private void createInteractPostNotification(String postId, String actorId, Instant timestamp, String receiverId, NotificationType notificationType) {

        boolean isExistNotification = true;

        // Upsert notification object
        Optional<NotificationObject> notificationObjOpt = notificationObjectRepository.findByNotificationTypeAndRef(notificationType, postId);
        NotificationObject savedNotificationObj;

        if (notificationObjOpt.isPresent()) {
            NotificationObject notificationObj = notificationObjOpt.get();
            notificationObj.setRead(false);
            savedNotificationObj = notificationObjectRepository.save(notificationObj);
        } else {
            NotificationObject obj = NotificationObject.builder()
                    .notificationType(notificationType)
                    .ref(postId)
                    .artifactId(postId)
                    .artifactType(ArtifactType.POST)
                    .isRead(false)
                    .build();
            try {
                isExistNotification = false;
                savedNotificationObj = notificationObjectRepository.save(obj);
            } catch (DataIntegrityViolationException e) {
                NotificationObject existedObj = notificationObjectRepository.findByNotificationTypeAndRef(notificationType, postId)
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

    private void undoInteractPostNotification(String postId, String actorId, NotificationType notificationType) {

        Optional<NotificationObject> notificationObjOpt = notificationObjectRepository.findByNotificationTypeAndRef(notificationType, postId);

        if (notificationObjOpt.isEmpty()) {
            return;
        }

        NotificationObject notificationObj = notificationObjOpt.get();

        notificationChangeRepository.deleteFirstByNotificationObjectIdAndActorId(notificationObj.getId(), actorId);
    }

    @Override
    @Transactional
    public void createFollowNotification(String followingId, String followerId, String followeeId, Instant timestamp) {
        createFollowUserNotification(followingId, followerId, followeeId, timestamp, NotificationType.FOLLOW);
    }

    @Override
    @Transactional
    public void undoFollowNotification(String followingId) {
        undoFollowUserNotification(followingId, NotificationType.FOLLOW);
    }

    @Override
    @Transactional
    public void createRequestFollowNotification(String followingId, String followerId, String followeeId, Instant timestamp) {
        createFollowUserNotification(followingId, followerId, followeeId, timestamp, NotificationType.REQUEST_FOLLOW);
    }

    @Override
    @Transactional
    public void undoRequestFollowNotification(String followingId) {
        undoFollowUserNotification(followingId, NotificationType.REQUEST_FOLLOW);
    }

    @Override
    @Transactional
    public void createAcceptFollowNotification(String followingId, String followerId, String followeeId, Instant timestamp) {
        createFollowUserNotification(followingId, followerId, followeeId, timestamp, NotificationType.ACCEPT_FOLLOW);
    }

    private void createFollowUserNotification(String followingId, String followerId, String followeeId, Instant timestamp, NotificationType notificationType) {
        NotificationObject obj = NotificationObject.builder()
                .notificationType(notificationType)
                .ref(followingId)
                .artifactId(followerId)
                .artifactType(ArtifactType.USER)
                .isRead(false)
                .build();

        NotificationObject savedNotificationObj = notificationObjectRepository.save(obj);

        NotificationChange notificationChange = NotificationChange.builder()
                .notificationObjectId(savedNotificationObj.getId())
                .actorId(followerId)
                .changeAt(timestamp)
                .build();

        notificationChangeRepository.save(notificationChange);

        Notification notification = Notification.builder()
                .notificationObjectId(savedNotificationObj.getId())
                .receiverId(followeeId)
                .build();

        notificationRepository.save(notification);
    }

    private void undoFollowUserNotification(String followingId, NotificationType notificationType) {
        notificationObjectRepository.deleteByNotificationTypeAndRef(notificationType, followingId);
    }

    @Override
    @Transactional
    public void createApprovePostNotification(String postId, Instant timestamp, String receiverId) {
        createModeratePostNotification(postId, timestamp, receiverId, NotificationType.APPROVE_POST);
    }

    @Override
    @Transactional
    public void createRejectPostNotification(String postId, Instant timestamp, String receiverId) {
        createModeratePostNotification(postId, timestamp, receiverId, NotificationType.REJECT_POST);
    }

    private void createModeratePostNotification(String postId, Instant timestamp, String receiverId, NotificationType notificationType) {

        boolean isExistNotification = true;

        Optional<NotificationObject> notificationObjOpt = notificationObjectRepository.findByNotificationTypeAndRef(notificationType, postId);
        NotificationObject savedNotificationObj;

        if (notificationObjOpt.isPresent()) {
            NotificationObject notificationObj = notificationObjOpt.get();
            notificationObj.setRead(false);
            savedNotificationObj = notificationObjectRepository.save(notificationObj);
        } else {
            NotificationObject obj = NotificationObject.builder()
                    .notificationType(notificationType)
                    .ref(postId)
                    .artifactId(postId)
                    .artifactType(ArtifactType.ROOM)
                    .isRead(false)
                    .build();
            try {
                isExistNotification = false;
                savedNotificationObj = notificationObjectRepository.save(obj);
            } catch (DataIntegrityViolationException e) {
                NotificationObject existedObj = notificationObjectRepository.findByNotificationTypeAndRef(notificationType, postId)
                        .orElseThrow(() -> new RuntimeException("Failed to resolve race condition"));
                existedObj.setRead(false);

                isExistNotification = true;
                savedNotificationObj = notificationObjectRepository.save(existedObj);
            }
        }

        NotificationChange notificationChange = NotificationChange.builder()
                .notificationObjectId(savedNotificationObj.getId())
                .actorId(null)
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

    @Override
    @Transactional
    public void createNewPostNotification(String roomId, String actorId, Instant timestamp, List<String> receiverIds) {

        boolean isExistNotification = true;

        // Upsert notification object
        Optional<NotificationObject> notificationObjOpt = notificationObjectRepository.findByNotificationTypeAndRef(NotificationType.NEW_PENDING_POST, roomId);
        NotificationObject savedNotificationObj;

        if (notificationObjOpt.isPresent()) {
            NotificationObject notificationObj = notificationObjOpt.get();
            notificationObj.setRead(false);
            savedNotificationObj = notificationObjectRepository.save(notificationObj);
        } else {
            NotificationObject obj = NotificationObject.builder()
                    .notificationType(NotificationType.NEW_PENDING_POST)
                    .ref(roomId)
                    .artifactId(roomId)
                    .artifactType(ArtifactType.ROOM)
                    .isRead(false)
                    .build();
            try {
                isExistNotification = false;
                savedNotificationObj = notificationObjectRepository.save(obj);
            } catch (DataIntegrityViolationException e) {
                NotificationObject existedObj = notificationObjectRepository.findByNotificationTypeAndRef(NotificationType.NEW_PENDING_POST, roomId)
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
            NotificationObject finalSavedNotificationObj = savedNotificationObj;
            List<Notification> notifications = receiverIds.stream()
                    .map(receiverId -> Notification.builder()
                            .notificationObjectId(finalSavedNotificationObj.getId())
                            .receiverId(receiverId)
                            .build())
                    .toList();
            notificationRepository.saveAll(notifications);
        }

        else {
            List<String> oldReceiverIds = notificationRepository.findReceiverIdsByNotificationObjectId(savedNotificationObj.getId());
            // Remove old receiver
            List<String> removedReceiverIds = oldReceiverIds.stream()
                    .filter(oldReceiverId -> !receiverIds.contains(oldReceiverId))
                    .toList();

            if (!removedReceiverIds.isEmpty()) {
                notificationRepository.deleteByNotificationObjectIdAndReceiverIdIn(savedNotificationObj.getId(), removedReceiverIds);
            }

            List<String> newReceiverIds = receiverIds.stream()
                    .filter(newReceiverId -> !oldReceiverIds.contains(newReceiverId))
                    .toList();

            if (newReceiverIds.isEmpty()) {
                return;
            }

            NotificationObject finalSavedNotificationObj = savedNotificationObj;
            List<Notification> notifications = newReceiverIds.stream()
                    .map(receiverId -> Notification.builder()
                            .notificationObjectId(finalSavedNotificationObj.getId())
                            .receiverId(receiverId)
                            .build())
                    .toList();

            notificationRepository.saveAll(notifications);
        }
    }

    @Override
    @Transactional
    public void undoNewPostNotification(String roomId, String actorId) {
        NotificationObject notificationObj = notificationObjectRepository.findByNotificationTypeAndRef(NotificationType.NEW_PENDING_POST, roomId)
                .orElseThrow(() -> new RuntimeException("Notification object not found"));

        notificationChangeRepository.deleteFirstByNotificationObjectIdAndActorId(notificationObj.getId(), actorId);
    }
}
