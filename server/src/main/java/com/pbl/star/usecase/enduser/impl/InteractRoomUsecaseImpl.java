package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.dtos.query.room.RoomForUserDTO;
import com.pbl.star.services.domain.RoomService;
import com.pbl.star.services.domain.UserRoomService;
import com.pbl.star.usecase.enduser.InteractRoomUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InteractRoomUsecaseImpl implements InteractRoomUsecase {

    private final UserRoomService userRoomService;
    private final RoomService roomService;

    @Override
    public List<RoomForUserDTO> getAllRoomsForUser() {
        return roomService.getRoomsOverviewForUser();
    }
    @Override
    public void joinRoom(String roomId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        userRoomService.addMemberToRoom(currentUserId, roomId);
    }

    @Override
    public void leaveRoom(String roomId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        userRoomService.removeMemberFromRoom(currentUserId, roomId);
    }
}
