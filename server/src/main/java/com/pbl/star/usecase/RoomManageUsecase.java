package com.pbl.star.usecase;

import com.pbl.star.dtos.query.room.RoomDetailsForAdminDTO;
import com.pbl.star.dtos.query.room.RoomForAdminDTO;
import com.pbl.star.dtos.query.room.RoomForUserDTO;
import com.pbl.star.dtos.query.user.UserInRoom;
import com.pbl.star.dtos.request.room.CreateRoomParams;

import java.util.List;

public interface RoomManageUsecase {
    RoomDetailsForAdminDTO getRoomDetails(String roomId);
    List<RoomForAdminDTO> getAllRooms();
    List<RoomForUserDTO> getAllRoomsForUser();
    String createRoom(CreateRoomParams params);
    void deleteRoom(String roomId);
    void updateRoom(String roomId, CreateRoomParams params);
    List<UserInRoom> getModerators(String roomId);
    void addModeratorById(String roomId, String userId);
    void addModeratorByUsername(String roomId, String username);
    void removeModerator(String roomId, String userId);
    List<UserInRoom> getMembers(String roomId, String keyword);
}
