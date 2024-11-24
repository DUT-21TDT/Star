package com.pbl.star.repositories;

import com.pbl.star.entities.NotificationObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationObjectRepository extends JpaRepository<NotificationObject, String> {
}
