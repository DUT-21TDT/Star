package com.pbl.star.repositories;

import com.pbl.star.entities.Notification;
import com.pbl.star.repositories.extensions.NotificationRepositoryExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String>, NotificationRepositoryExtension {
}
