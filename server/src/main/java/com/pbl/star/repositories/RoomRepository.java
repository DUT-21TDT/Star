package com.pbl.star.repositories;

import com.pbl.star.models.entities.Room;
import com.pbl.star.repositories.extensions.RoomRepositoryExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, String>, RoomRepositoryExtension {
    Boolean existsByNameIgnoreCase(String name);
}
