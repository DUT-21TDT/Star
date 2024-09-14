package com.pbl.star.services;

import com.pbl.star.dtos.query.room.RoomOverviewDTO;
import com.pbl.star.dtos.request.room.CreateRoomParams;

import java.util.List;

public interface RoomService {
    List<RoomOverviewDTO> getRoomsOverview();
    String createRoom(CreateRoomParams params);
}
