package com.pbl.star.usecase.enduser;

import com.pbl.star.dtos.query.room.RoomForUserDTO;

import java.util.List;

public interface InteractRoomUsecase {
    List<RoomForUserDTO> getAllRoomsForUser();
    void joinRoom(String roomId);
    void leaveRoom(String roomId);
}
