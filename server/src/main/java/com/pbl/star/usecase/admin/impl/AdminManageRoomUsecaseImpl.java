package com.pbl.star.usecase.admin.impl;

import com.pbl.star.dtos.response.room.RoomDetailsForAdminResponse;
import com.pbl.star.dtos.response.room.RoomForAdminResponse;
import com.pbl.star.mapper.room.RoomDTOMapper;
import com.pbl.star.dtos.request.room.CreateRoomParams;
import com.pbl.star.services.domain.RoomService;
import com.pbl.star.usecase.admin.AdminManageRoomUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminManageRoomUsecaseImpl implements AdminManageRoomUsecase {

    private final RoomService roomService;

    private final RoomDTOMapper roomMapper;

    @Override
    public RoomDetailsForAdminResponse getRoomDetails(String roomId) {
        return roomMapper.toDTO(roomService.getRoomDetails(roomId));
    }

    @Override
    public List<RoomForAdminResponse> getAllRooms() {
        return roomService.getRoomsOverview()
                .stream().map(roomMapper::toDTO).toList();
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
