package com.pbl.star.services.domain;

import com.pbl.star.dtos.query.room.RoomOverviewDTO;
import com.pbl.star.dtos.query.room.RoomOverviewForUserDTO;
import com.pbl.star.dtos.request.room.CreateRoomParams;
import com.pbl.star.entities.Room;

import java.util.List;

public interface RoomService {
    List<RoomOverviewDTO> getRoomsOverview();
    List<RoomOverviewForUserDTO> getRoomsOverviewForUser();
    Room createRoom(CreateRoomParams params);
    void deleteRoom(String roomId);
    void updateRoom(String roomId, CreateRoomParams params);
    void joinRoom(String userId, String roomId);
    void leaveRoom(String userId, String roomId);
}
