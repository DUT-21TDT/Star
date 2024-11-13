package com.pbl.star.usecase.admin;

import com.pbl.star.dtos.query.user.UserInRoom;

import java.util.List;

public interface AdminManageRoomMemberUsecase {
    List<UserInRoom> getModerators(String roomId);
    void addModeratorById(String roomId, String userId);
    void addModeratorByUsername(String roomId, String username);
    void removeModerator(String roomId, String userId);
    List<UserInRoom> getMembers(String roomId, String keyword);
}
