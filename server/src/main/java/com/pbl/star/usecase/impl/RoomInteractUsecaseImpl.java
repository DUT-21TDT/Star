package com.pbl.star.usecase.impl;

import com.pbl.star.services.domain.RoomService;
import com.pbl.star.usecase.RoomInteractUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomInteractUsecaseImpl implements RoomInteractUsecase {

    private final RoomService roomService;
    @Override
    public void joinRoom(String roomId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        roomService.joinRoom(currentUserId, roomId);
    }

    @Override
    public void leaveRoom(String roomId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        roomService.leaveRoom(currentUserId, roomId);
    }
}
