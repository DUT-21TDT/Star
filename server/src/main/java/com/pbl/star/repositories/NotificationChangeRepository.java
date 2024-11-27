package com.pbl.star.repositories;

import com.pbl.star.entities.NotificationChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NotificationChangeRepository extends JpaRepository<NotificationChange, String> {
    @Modifying
    @Transactional
    @Query("DELETE FROM NotificationChange nc WHERE nc.id = (" +
            "SELECT MIN(nc2.id) FROM NotificationChange nc2 WHERE nc2.notificationObjectId = ?1 AND nc2.actorId = ?2)")
    void deleteFirstByNotificationObjectIdAndActorId(String notificationObjectId, String actorId);
}
