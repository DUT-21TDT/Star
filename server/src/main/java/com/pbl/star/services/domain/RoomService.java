package com.pbl.star.services.domain;

import com.pbl.star.dtos.query.room.RoomDetailsForAdminDTO;
import com.pbl.star.dtos.query.room.RoomForAdminDTO;
import com.pbl.star.dtos.query.room.RoomForUserDTO;
import com.pbl.star.dtos.request.room.CreateRoomParams;
import com.pbl.star.entities.Room;

import java.util.List;

public interface RoomService {
    RoomDetailsForAdminDTO getRoomDetails(String roomId);
    List<RoomForAdminDTO> getRoomsOverview();
    List<RoomForUserDTO> getRoomsOverviewForUser();
    Room createRoom(CreateRoomParams params);
    void deleteRoom(String roomId);
    void updateRoom(String roomId, CreateRoomParams params);
}
