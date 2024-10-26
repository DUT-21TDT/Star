package com.pbl.star.usecase.impl;

import com.pbl.star.services.domain.UserRoomService;
import com.pbl.star.usecase.RoomInteractUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomInteractUsecaseImpl implements RoomInteractUsecase {

    private final UserRoomService userRoomService;
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
