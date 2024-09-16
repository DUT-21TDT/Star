package com.pbl.star.repositories;

import com.pbl.star.entities.Room;
import com.pbl.star.entities.User;
import com.pbl.star.entities.UserRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoomRepository extends JpaRepository<UserRoom, String> {
    @Query("SELECT COUNT(*) > 0 FROM UserRoom ur WHERE ur.user = ?1 AND ur.room = ?2")
    boolean existsByUserAndRoom(User user, Room room);

    Optional<UserRoom> findByUserIdAndRoomId(String userId, String roomId);
}
