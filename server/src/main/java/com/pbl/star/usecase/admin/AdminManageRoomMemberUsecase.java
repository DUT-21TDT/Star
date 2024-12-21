package com.pbl.star.usecase.admin;

import com.pbl.star.dtos.response.user.UserInRoomResponse;
import com.pbl.star.models.projections.user.UserInRoom;

import java.util.List;

public interface AdminManageRoomMemberUsecase {
    List<UserInRoomResponse> getModerators(String roomId);
    void addModeratorById(String roomId, String userId);
    void removeModerator(String roomId, String userId);
    List<UserInRoomResponse> getMembers(String roomId, String keyword);
}
