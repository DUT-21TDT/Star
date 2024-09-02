package com.pbl.star.repositories;

import com.pbl.star.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    Boolean existsByNameIgnoreCase(String name);
}
