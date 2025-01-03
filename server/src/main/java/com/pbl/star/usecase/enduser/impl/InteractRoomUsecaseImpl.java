package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.dtos.response.room.RoomForUserResponse;
import com.pbl.star.enums.RoomRole;
import com.pbl.star.mapper.room.RoomDTOMapper;
import com.pbl.star.models.entities.UserRoom;
import com.pbl.star.services.domain.NotificationService;
import com.pbl.star.services.domain.RoomService;
import com.pbl.star.services.domain.UserRoomService;
import com.pbl.star.usecase.enduser.InteractRoomUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class InteractRoomUsecaseImpl implements InteractRoomUsecase {

    private final UserRoomService userRoomService;
    private final NotificationService notificationService;
    private final RoomService roomService;

    private final RoomDTOMapper mapper;

    @Override
    public List<RoomForUserResponse> getAllRoomsForUser() {
        return roomService.getRoomsOverviewForUser()
                .stream().map(mapper::toDTO).toList();
    }
    @Override
    public void joinRoom(String roomId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        userRoomService.addMemberToRoom(currentUserId, roomId);
    }

    @Override
    public void leaveRoom(String roomId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        UserRoom deletedUserRoom = userRoomService.removeMemberFromRoom(currentUserId, roomId);

        if (deletedUserRoom.getRole().equals(RoomRole.MODERATOR)) {
            CompletableFuture.runAsync(() -> notificationService.deleteModNotifications(currentUserId, roomId));
        }
    }
}
