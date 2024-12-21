package com.pbl.star.usecase.enduser;

import com.pbl.star.dtos.response.room.RoomForUserResponse;
import com.pbl.star.models.projections.room.RoomForUser;

import java.util.List;

public interface InteractRoomUsecase {
    List<RoomForUserResponse> getAllRoomsForUser();
    void joinRoom(String roomId);
    void leaveRoom(String roomId);
}
