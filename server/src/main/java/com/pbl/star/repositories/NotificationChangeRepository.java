package com.pbl.star.repositories;

import com.pbl.star.entities.NotificationChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationChangeRepository extends JpaRepository<NotificationChange, String> {
}
