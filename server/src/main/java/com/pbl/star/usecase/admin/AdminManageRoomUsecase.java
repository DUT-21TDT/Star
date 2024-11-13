package com.pbl.star.usecase.admin;

import com.pbl.star.dtos.query.room.RoomDetailsForAdminDTO;
import com.pbl.star.dtos.query.room.RoomForAdminDTO;
import com.pbl.star.dtos.request.room.CreateRoomParams;

import java.util.List;

public interface AdminManageRoomUsecase {
    List<RoomForAdminDTO> getAllRooms();
    RoomDetailsForAdminDTO getRoomDetails(String roomId);
    String createRoom(CreateRoomParams params);
    void deleteRoom(String roomId);
    void updateRoom(String roomId, CreateRoomParams params);
}
