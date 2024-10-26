package com.pbl.star.usecase;

import com.pbl.star.dtos.query.room.RoomForAdminDTO;
import com.pbl.star.dtos.query.room.RoomForUserDTO;
import com.pbl.star.dtos.request.room.CreateRoomParams;

import java.util.List;

public interface RoomManageUsecase {
    List<RoomForAdminDTO> getAllRooms();
    List<RoomForUserDTO> getAllRoomsForUser();
    String createRoom(CreateRoomParams params);
    void deleteRoom(String roomId);
    void updateRoom(String roomId, CreateRoomParams params);
}
