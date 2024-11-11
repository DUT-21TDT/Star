package com.pbl.star.usecase.impl;

import com.pbl.star.dtos.query.room.RoomDetailsForAdminDTO;
import com.pbl.star.dtos.query.room.RoomForAdminDTO;
import com.pbl.star.dtos.query.room.RoomForUserDTO;
import com.pbl.star.dtos.query.user.UserInRoom;
import com.pbl.star.dtos.request.room.CreateRoomParams;
import com.pbl.star.services.domain.RoomService;
import com.pbl.star.services.domain.UserRoomService;
import com.pbl.star.usecase.RoomManageUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomManageUsecaseImpl implements RoomManageUsecase {

    private final RoomService roomService;
    private final UserRoomService userRoomService;

    @Override
    public RoomDetailsForAdminDTO getRoomDetails(String roomId) {
        return roomService.getRoomDetails(roomId);
    }

    @Override
    public List<RoomForAdminDTO> getAllRooms() {
        return roomService.getRoomsOverview();
    }

    @Override
    public List<RoomForUserDTO> getAllRoomsForUser() {
        return roomService.getRoomsOverviewForUser();
    }

    @Override
    public String createRoom(CreateRoomParams params) {
        return roomService.createRoom(params).getId();
    }

    @Override
    public void deleteRoom(String roomId) {
        roomService.deleteRoom(roomId);
    }

    @Override
    public void updateRoom(String roomId, CreateRoomParams params) {
        roomService.updateRoom(roomId, params);
    }

    @Override
    public List<UserInRoom> getModerators(String roomId) {
        return userRoomService.getModerators(roomId);
    }

    @Override
    public void addModeratorById(String roomId, String userId) {
        userRoomService.addModeratorToRoom(userId, roomId);
    }

    @Override
    public void addModeratorByUsername(String roomId, String username) {
        userRoomService.addModeratorToRoomByUsername(username, roomId);
    }

    @Override
    public void removeModerator(String roomId, String userId) {
        userRoomService.removeModeratorFromRoom(userId, roomId);
    }

    @Override
    public List<UserInRoom> getMembers(String roomId, String keyword) {
        return userRoomService.getMembers(roomId, keyword);
    }
}
