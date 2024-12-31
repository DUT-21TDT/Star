package com.pbl.star.repositories;

import com.pbl.star.models.entities.Notification;
import com.pbl.star.repositories.extensions.NotificationRepositoryExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String>, NotificationRepositoryExtension {
    @Query("SELECT n.receiverId FROM Notification n WHERE n.notificationObjectId = :notificationObjectId")
    List<String> findReceiverIdsByNotificationObjectId(String notificationObjectId);

    List<Notification> findNotificationsByNotificationObjectId(String notificationObjectId);

    Optional<Notification> findByNotificationObjectId(String notificationObjectId);

    void deleteByNotificationObjectIdAndReceiverIdIn(String notificationObjectId, List<String> receiverIds);

    @Query("DELETE FROM Notification n WHERE n.receiverId = ?2 AND " +
            "n.notificationObjectId IN " +
            "(SELECT n.notificationObjectId FROM NotificationObject nob " +
            "INNER JOIN Post p ON nob.artifactId = p.id " +
            "WHERE p.roomId = ?1 AND nob.notificationType in ('NEW_PENDING_POST', 'APPROVE_POST', 'REJECT_POST'))")
    void deleteModNotificationsByRoomIdAndUserId(String roomId, String userId);
}
