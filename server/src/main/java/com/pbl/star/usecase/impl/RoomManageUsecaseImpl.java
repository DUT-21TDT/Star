package com.pbl.star.usecase.impl;

import com.pbl.star.dtos.query.room.RoomDetailsForAdminDTO;
import com.pbl.star.dtos.query.room.RoomForAdminDTO;
import com.pbl.star.dtos.query.room.RoomForUserDTO;
import com.pbl.star.dtos.request.room.CreateRoomParams;
import com.pbl.star.services.domain.RoomService;
import com.pbl.star.usecase.RoomManageUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomManageUsecaseImpl implements RoomManageUsecase {

    private final RoomService roomService;

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
}
