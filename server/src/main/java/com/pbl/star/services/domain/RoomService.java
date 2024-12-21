package com.pbl.star.services.domain;

import com.pbl.star.models.projections.room.RoomDetailsForAdmin;
import com.pbl.star.models.projections.room.RoomForAdmin;
import com.pbl.star.models.projections.room.RoomForUser;
import com.pbl.star.dtos.request.room.CreateRoomParams;
import com.pbl.star.models.entities.Room;

import java.util.List;

public interface RoomService {
    RoomDetailsForAdmin getRoomDetails(String roomId);
    List<RoomForAdmin> getRoomsOverview();
    List<RoomForUser> getRoomsOverviewForUser();
    Room createRoom(CreateRoomParams params);
    void deleteRoom(String roomId);
    void updateRoom(String roomId, CreateRoomParams params);
}
