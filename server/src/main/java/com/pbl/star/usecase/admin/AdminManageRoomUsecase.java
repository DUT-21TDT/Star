package com.pbl.star.usecase.admin;

import com.pbl.star.dtos.response.room.RoomDetailsForAdminResponse;
import com.pbl.star.dtos.response.room.RoomForAdminResponse;
import com.pbl.star.dtos.request.room.CreateRoomParams;

import java.util.List;

public interface AdminManageRoomUsecase {
    List<RoomForAdminResponse> getAllRooms();
    RoomDetailsForAdminResponse getRoomDetails(String roomId);
    String createRoom(CreateRoomParams params);
    void deleteRoom(String roomId);
    void updateRoom(String roomId, CreateRoomParams params);
}
