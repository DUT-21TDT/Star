package com.pbl.star.usecase.impl;

import com.pbl.star.dtos.query.room.RoomOverviewDTO;
import com.pbl.star.dtos.request.room.CreateRoomParams;
import com.pbl.star.services.RoomService;
import com.pbl.star.usecase.RoomUsecase;
import com.pbl.star.utils.AuthUtil;
import com.pbl.star.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomUsecaseImpl implements RoomUsecase {

    private final RoomService roomService;

    @Override
    public List<RoomOverviewDTO> getAllRooms() {
        return roomService.getRoomsOverview();
    }

    @Override
    public String createRoom(CreateRoomParams params) {
        return roomService.createRoom(params);
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
    public void joinRoom(String roomId) {
        CurrentUser currentUser = AuthUtil.getCurrentUser();
        roomService.joinRoom(currentUser.getId(), roomId);
    }
}
