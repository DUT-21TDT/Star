package com.pbl.star.services.domain.impl;

import com.pbl.star.enums.ArtifactType;
import com.pbl.star.enums.NotificationType;
import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.exceptions.ResourceOwnershipException;
import com.pbl.star.models.entities.*;
import com.pbl.star.models.projections.notification.NotificationForUser;
import com.pbl.star.repositories.*;
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

    private final PostRepository postRepository;
    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;

    private final NotificationRepository notificationRepository;
    private final NotificationObjectRepository notificationObjectRepository;
    private final NotificationChangeRepository notificationChangeRepository;

    @Override
    public Slice<NotificationForUser> getNotifications(String userId, int limit, Instant after) {
        List<NotificationForUser> notiList = notificationRepository.getNotifications(limit + 1, after, userId);
        return SliceTransfer.trimToSlice(notiList, limit);
    }

    @Override
    @Transactional
    public void markAsRead(String userId, String notificationId) {
        Notification noti = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        if (!noti.getReceiverId().equals(userId)) {
            throw new ResourceOwnershipException("Notification does not belong to the user");
        }

        if (noti.isRead()) {
            return;
        }

        noti.setRead(true);
        notificationRepository.save(noti);
    }

    @Override
    public NotificationForUser getPushedNotification(String notificationObjId) {
        return notificationRepository.getNotificationByNotificationObjectId(notificationObjId)
                .orElse(null);
    }

    @Override
    @Transactional
    public Notification createInteractPostNotification(String postId, String actorId, Instant timestamp, NotificationType notificationType) {

        Post interactedPost = postRepository.findExistPostById(postId)
                .orElse(null);

        if (interactedPost == null) {
            return null;
        }

        String receiverId = interactedPost.getUserId();

        if (actorId.equals(receiverId)) {
            return null;
        }

        String preview = getPostPreview(interactedPost.getContent());
        boolean isExistNotification = true;

        // Upsert notification object
        Optional<NotificationObject> notificationObjOpt = notificationObjectRepository.findByNotificationTypeAndRef(notificationType, postId);
        NotificationObject savedNotificationObj;

        if (notificationObjOpt.isPresent()) {
            savedNotificationObj = notificationObjOpt.get();
        } else {
            NotificationObject obj = NotificationObject.builder()
                    .notificationType(notificationType)
                    .ref(postId)
                    .artifactId(postId)
                    .artifactType(ArtifactType.POST)
                    .artifactPreview(preview)
                    .build();
            try {
                isExistNotification = false;
                savedNotificationObj = notificationObjectRepository.save(obj);
            } catch (DataIntegrityViolationException e) {
                savedNotificationObj = notificationObjectRepository.findByNotificationTypeAndRef(notificationType, postId)
                        .orElseThrow(() -> new RuntimeException("Failed to resolve race condition"));
                isExistNotification = true;
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
                    .isRead(false)
                    .build();
            return notificationRepository.save(notification);
        }

        Notification notification = notificationRepository.findByNotificationObjectId(savedNotificationObj.getId())
                .orElse(null);

        if (notification != null) {
            notification.setRead(false);
            return notificationRepository.save(notification);
        }

        return null;
    }

    @Override
    @Transactional
    public void undoInteractPostNotification(String postId, String actorId, NotificationType notificationType) {

        Optional<NotificationObject> notificationObjOpt = notificationObjectRepository.findByNotificationTypeAndRef(notificationType, postId);

        if (notificationObjOpt.isEmpty()) {
            return;
        }

        NotificationObject notificationObj = notificationObjOpt.get();

        notificationChangeRepository.deleteFirstByNotificationObjectIdAndActorId(notificationObj.getId(), actorId);
    }

    public Notification createFollowUserNotification(String followingId, String followerId, String followeeId, Instant timestamp, NotificationType notificationType) {
        NotificationObject obj = NotificationObject.builder()
                .notificationType(notificationType)
                .ref(followingId)
                .artifactId(followerId)
                .artifactType(ArtifactType.USER)
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

        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void undoFollowUserNotification(String followingId, NotificationType notificationType) {
        notificationObjectRepository.deleteByNotificationTypeAndRef(notificationType, followingId);
    }

    @Override
    @Transactional
    public Notification createModeratePostNotification(String postId, Instant timestamp, NotificationType notificationType) {

        Post moderatedPost = postRepository.findExistPostById(postId)
                .orElse(null);

        if (moderatedPost == null) {
            return null;
        }

        String receiverId = moderatedPost.getUserId();
        String preview = getPostPreview(moderatedPost.getContent());

        boolean isExistNotification = true;

        Optional<NotificationObject> notificationObjOpt = notificationObjectRepository.findByNotificationTypeAndRef(notificationType, postId);
        NotificationObject savedNotificationObj;

        if (notificationObjOpt.isPresent()) {
            savedNotificationObj = notificationObjOpt.get();
        } else {
            NotificationObject obj = NotificationObject.builder()
                    .notificationType(notificationType)
                    .ref(postId)
                    .artifactId(postId)
                    .artifactType(ArtifactType.POST)
                    .artifactPreview(preview)
                    .build();
            try {
                isExistNotification = false;
                savedNotificationObj = notificationObjectRepository.save(obj);
            } catch (DataIntegrityViolationException e) {
                savedNotificationObj = notificationObjectRepository.findByNotificationTypeAndRef(notificationType, postId)
                        .orElseThrow(() -> new RuntimeException("Failed to resolve race condition"));

                isExistNotification = true;
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
            return notificationRepository.save(notification);
        }

        return Notification.builder()
                .receiverId(receiverId)
                .notificationObjectId(savedNotificationObj.getId())
                .build();
    }

    private String getPostPreview(String content) {
        if (content == null) {
            return "";
        }

        return content.length() > 100 ? content.substring(0, 100) : content;
    }

    @Override
    @Transactional
    public List<Notification> createNewPostNotification(String roomId, String actorId, Instant timestamp) {

        Room room = roomRepository.findById(roomId)
                .orElse(null);

        if (room == null) {
            return null;
        }

        List<String> receivers = userRoomRepository.findModeratorIdsByRoomId(roomId);

        if (receivers.isEmpty()) {
            return null;
        }

        boolean isExistNotification = true;

        // Upsert notification object
        Optional<NotificationObject> notificationObjOpt = notificationObjectRepository.findByNotificationTypeAndRef(NotificationType.NEW_PENDING_POST, roomId);
        NotificationObject savedNotificationObj;

        if (notificationObjOpt.isPresent()) {
            savedNotificationObj = notificationObjOpt.get();
        } else {
            NotificationObject obj = NotificationObject.builder()
                    .notificationType(NotificationType.NEW_PENDING_POST)
                    .ref(roomId)
                    .artifactId(roomId)
                    .artifactType(ArtifactType.ROOM)
                    .artifactPreview(room.getName())
                    .build();
            try {
                isExistNotification = false;
                savedNotificationObj = notificationObjectRepository.save(obj);
            } catch (DataIntegrityViolationException e) {
                savedNotificationObj = notificationObjectRepository.findByNotificationTypeAndRef(NotificationType.NEW_PENDING_POST, roomId)
                        .orElseThrow(() -> new RuntimeException("Failed to resolve race condition"));

                isExistNotification = true;
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
            NotificationObject tmpSavedNotificationObj = savedNotificationObj;
            List<Notification> notifications = receivers.stream()
                    .map(receiverId -> Notification.builder()
                            .notificationObjectId(tmpSavedNotificationObj.getId())
                            .receiverId(receiverId)
                            .isRead(false)
                            .build())
                    .toList();
            return notificationRepository.saveAll(notifications);
        } else {
            List<Notification> oldNotifications = notificationRepository.findNotificationsByNotificationObjectId(savedNotificationObj.getId());
            List<String> oldReceivers = oldNotifications.stream()
                    .map(Notification::getReceiverId)
                    .toList();
            // Remove old receiver
            List<String> removedReceiverIds = oldReceivers.stream()
                    .filter(receiverId -> !receivers.contains(receiverId))
                    .toList();

            if (!removedReceiverIds.isEmpty()) {
                notificationRepository.deleteByNotificationObjectIdAndReceiverIdIn(savedNotificationObj.getId(), removedReceiverIds);
            }

            for (String receiver : receivers) {
                if (!oldReceivers.contains(receiver)) {
                    Notification notification = Notification.builder()
                            .notificationObjectId(savedNotificationObj.getId())
                            .receiverId(receiver)
                            .isRead(false)
                            .build();
                    oldNotifications.add(notification);
                } else {
                    Notification notification = oldNotifications.stream()
                            .filter(noti -> noti.getReceiverId().equals(receiver))
                            .findFirst()
                            .orElse(null);
                    assert notification != null;
                    notification.setRead(false);
                }
            }

            return notificationRepository.saveAll(oldNotifications);
        }
    }

    @Override
    @Transactional
    public void undoNewPostNotification(String roomId, String actorId) {
        NotificationObject notificationObj = notificationObjectRepository.findByNotificationTypeAndRef(NotificationType.NEW_PENDING_POST, roomId)
                .orElseThrow(() -> new RuntimeException("Notification object not found"));

        notificationChangeRepository.deleteFirstByNotificationObjectIdAndActorId(notificationObj.getId(), actorId);
    }

    @Override
    public List<Notification> reportPostNotification(String postId, String actorId, Instant timestamp) {
        Post reportedPost = postRepository.findExistPostById(postId)
                .orElse(null);

        if (reportedPost == null) {
            return null;
        }

        String roomId = reportedPost.getRoomId();
        Room room = roomRepository.findById(roomId)
                .orElse(null);

        if (room == null) {
            return null;
        }

        List<String> receivers = userRoomRepository.findModeratorIdsByRoomId(roomId);

        if (receivers.isEmpty()) {
            return null;
        }

        boolean isExistNotification = true;

        // Upsert notification object
        Optional<NotificationObject> notificationObjOpt = notificationObjectRepository.findByNotificationTypeAndRef(NotificationType.REPORT_POST, postId);
        NotificationObject savedNotificationObj;

        if (notificationObjOpt.isPresent()) {
            savedNotificationObj = notificationObjOpt.get();
        } else {
            NotificationObject obj = NotificationObject.builder()
                    .notificationType(NotificationType.REPORT_POST)
                    .ref(postId)
                    .artifactId(roomId)
                    .artifactType(ArtifactType.ROOM)
                    .artifactPreview(getPostPreview(reportedPost.getContent()))
                    .build();
            try {
                isExistNotification = false;
                savedNotificationObj = notificationObjectRepository.save(obj);
            } catch (DataIntegrityViolationException e) {
                savedNotificationObj = notificationObjectRepository.findByNotificationTypeAndRef(NotificationType.REPORT_POST, roomId)
                        .orElseThrow(() -> new RuntimeException("Failed to resolve race condition"));

                isExistNotification = true;
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
            NotificationObject tmpSavedNotificationObj = savedNotificationObj;
            List<Notification> notifications = receivers.stream()
                    .map(receiverId -> Notification.builder()
                            .notificationObjectId(tmpSavedNotificationObj.getId())
                            .receiverId(receiverId)
                            .isRead(false)
                            .build())
                    .toList();
            return notificationRepository.saveAll(notifications);
        } else {
            List<Notification> oldNotifications = notificationRepository.findNotificationsByNotificationObjectId(savedNotificationObj.getId());
            List<String> oldReceivers = oldNotifications.stream()
                    .map(Notification::getReceiverId)
                    .toList();
            // Remove old receiver
            List<String> removedReceiverIds = oldReceivers.stream()
                    .filter(receiverId -> !receivers.contains(receiverId))
                    .toList();

            if (!removedReceiverIds.isEmpty()) {
                notificationRepository.deleteByNotificationObjectIdAndReceiverIdIn(savedNotificationObj.getId(), removedReceiverIds);
            }

            for (String receiver : receivers) {
                if (!oldReceivers.contains(receiver)) {
                    Notification notification = Notification.builder()
                            .notificationObjectId(savedNotificationObj.getId())
                            .receiverId(receiver)
                            .isRead(false)
                            .build();
                    oldNotifications.add(notification);
                } else {
                    Notification notification = oldNotifications.stream()
                            .filter(noti -> noti.getReceiverId().equals(receiver))
                            .findFirst()
                            .orElse(null);
                    assert notification != null;
                    notification.setRead(false);
                }
            }

            return notificationRepository.saveAll(oldNotifications);
        }
    }

    @Override
    @Transactional
    public void deleteModNotifications(String roomId, String userId) {
        notificationRepository.deleteModNotificationsByRoomIdAndUserId(roomId, userId);
    }
}
