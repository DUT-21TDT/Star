package com.pbl.star.repositories;

import com.pbl.star.entities.NotificationObject;
import com.pbl.star.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationObjectRepository extends JpaRepository<NotificationObject, String> {
    Optional<NotificationObject> findByNotificationTypeAndArtifactId(NotificationType notificationType, String artifactId);
}
