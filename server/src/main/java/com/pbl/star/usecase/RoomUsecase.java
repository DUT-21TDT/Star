package com.pbl.star.usecase;

import com.pbl.star.dtos.request.room.CreateRoomParams;

public interface RoomUsecase {
    String createRoom(CreateRoomParams params);
}
