package com.pbl.star.usecase.impl;

import com.pbl.star.services.RoomService;
import com.pbl.star.usecase.RoomInteractUsecase;
import com.pbl.star.utils.AuthUtil;
import com.pbl.star.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomInteractUsecaseImpl implements RoomInteractUsecase {

    private final RoomService roomService;
    @Override
    public void joinRoom(String roomId) {
        CurrentUser currentUser = AuthUtil.getCurrentUser();
        roomService.joinRoom(currentUser.getId(), roomId);
    }

    @Override
    public void leaveRoom(String roomId) {
        CurrentUser currentUser = AuthUtil.getCurrentUser();
        roomService.leaveRoom(currentUser.getId(), roomId);
    }
}
