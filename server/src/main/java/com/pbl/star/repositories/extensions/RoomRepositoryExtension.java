package com.pbl.star.repositories.extensions;

import com.pbl.star.models.projections.room.RoomDetailsForAdmin;
import com.pbl.star.models.projections.room.RoomForAdmin;
import com.pbl.star.models.projections.room.RoomForUser;
import com.pbl.star.models.projections.user.UserInRoom;

import java.util.List;

public interface RoomRepositoryExtension {
    List<RoomForAdmin> getRoomsOverview();
    RoomDetailsForAdmin getRoomDetails(String roomId);
    List<UserInRoom> getModsInRoom(String roomId);
    List<RoomForUser> getRoomsOverviewForUser(String userId);
}
