package com.pbl.star.usecase.admin.impl;

import com.pbl.star.dtos.query.user.UserInRoom;
import com.pbl.star.services.domain.UserRoomService;
import com.pbl.star.usecase.admin.AdminManageRoomMemberUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminManageRoomMemberUsecaseImpl implements AdminManageRoomMemberUsecase {

    private final UserRoomService userRoomService;

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
