package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.room.RoomDetailsForAdminDTO;
import com.pbl.star.dtos.query.room.RoomForAdminDTO;
import com.pbl.star.dtos.query.room.RoomForUserDTO;
import com.pbl.star.dtos.query.user.ModInRoom;

import java.util.List;

public interface RoomRepositoryExtension {
    List<RoomForAdminDTO> getRoomsOverview();
    RoomDetailsForAdminDTO getRoomDetails(String roomId);
    List<ModInRoom> getModsInRoom(String roomId);
    List<RoomForUserDTO> getRoomsOverviewForUser(String userId);
}
