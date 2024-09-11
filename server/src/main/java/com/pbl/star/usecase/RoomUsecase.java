package com.pbl.star.usecase;

import com.pbl.star.dtos.query.room.RoomOverviewDTO;
import com.pbl.star.dtos.request.room.CreateRoomParams;

import java.util.List;

public interface RoomUsecase {
    List<RoomOverviewDTO> getAllRooms();
    String createRoom(CreateRoomParams params);
    void deleteRoom(String roomId);
    void updateRoom(String roomId, CreateRoomParams params);
}
