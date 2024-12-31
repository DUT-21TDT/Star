package com.pbl.star.usecase.admin.impl;

import com.pbl.star.dtos.response.user.UserInRoomResponse;
import com.pbl.star.mapper.user.UserDTOMapper;
import com.pbl.star.models.entities.UserRoom;
import com.pbl.star.services.domain.NotificationService;
import com.pbl.star.services.domain.UserRoomService;
import com.pbl.star.usecase.admin.AdminManageRoomMemberUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminManageRoomMemberUsecaseImpl implements AdminManageRoomMemberUsecase {
    // Services
    private final UserRoomService userRoomService;
    private final NotificationService notificationService;

    // Mappers
    private final UserDTOMapper userMapper;
    @Override
    public List<UserInRoomResponse> getModerators(String roomId) {
        return userRoomService.getModerators(roomId)
                .stream().map(userMapper::toDTO).toList();
    }

    @Override
    public void addModeratorById(String roomId, String userId) {
        userRoomService.addModeratorToRoom(userId, roomId);
    }

    @Override
    public void removeModerator(String roomId, String userId) {
        UserRoom userRoom = userRoomService.removeModeratorFromRoom(userId, roomId);

        notificationService.deleteModNotifications(userRoom.getRoomId(), userRoom.getUserId());
    }

    @Override
    public List<UserInRoomResponse> getMembers(String roomId, String keyword) {
        return userRoomService.getMembers(roomId, keyword)
                .stream().map(userMapper::toDTO).toList();
    }
}
