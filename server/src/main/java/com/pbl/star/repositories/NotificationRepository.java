package com.pbl.star.repositories;

import com.pbl.star.entities.Notification;
import com.pbl.star.repositories.extensions.NotificationRepositoryExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String>, NotificationRepositoryExtension {
    @Query("SELECT n.receiverId FROM Notification n WHERE n.notificationObjectId = :notificationObjectId")
    List<String> findReceiverIdsByNotificationObjectId(String notificationObjectId);

    void deleteByNotificationObjectIdAndReceiverIdIn(String notificationObjectId, List<String> receiverIds);
}
