package com.pbl.star.services;

import com.pbl.star.dtos.request.room.CreateRoomParams;

public interface RoomService {
    String createRoom(CreateRoomParams params);
}
