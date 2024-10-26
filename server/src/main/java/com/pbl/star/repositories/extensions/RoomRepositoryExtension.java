package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.room.RoomForAdminDTO;
import com.pbl.star.dtos.query.room.RoomForUserDTO;

import java.util.List;

public interface RoomRepositoryExtension {
    List<RoomForAdminDTO> getRoomsOverview();
    List<RoomForUserDTO> getRoomsOverviewForUser(String userId);
}
