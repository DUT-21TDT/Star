package com.pbl.star.repositories;

import com.pbl.star.models.entities.NotificationObject;
import com.pbl.star.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationObjectRepository extends JpaRepository<NotificationObject, String> {
    Optional<NotificationObject> findByNotificationTypeAndRef(NotificationType notificationType, String refId);

    void deleteByNotificationTypeAndRef(NotificationType notificationType, String refId);
}
