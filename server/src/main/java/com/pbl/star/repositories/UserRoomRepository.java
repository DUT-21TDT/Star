package com.pbl.star.repositories;

import com.pbl.star.entities.UserRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoomRepository extends JpaRepository<UserRoom, String> {
    boolean existsByUserIdAndRoomId(String userId, String roomId);
    Optional<UserRoom> findByUserIdAndRoomId(String userId, String roomId);

    @Query("SELECT DISTINCT ur.roomId FROM UserRoom ur WHERE ur.userId = :userId")
    List<String> findRoomIdsByUserId(String userId);
}
