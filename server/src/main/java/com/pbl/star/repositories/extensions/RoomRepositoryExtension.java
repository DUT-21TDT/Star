package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.room.RoomOverviewDTO;
import com.pbl.star.dtos.query.room.RoomOverviewForUserDTO;

import java.util.List;

public interface RoomRepositoryExtension {
    List<RoomOverviewDTO> getRoomsOverview();
    List<RoomOverviewForUserDTO> getRoomsOverviewForUser(String userId);
}
