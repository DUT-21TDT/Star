package com.pbl.star.services.domain;

import com.pbl.star.dtos.query.user.UserInRoom;
import org.springframework.lang.NonNull;

import java.util.List;

public interface UserRoomService {
    void addMemberToRoom(@NonNull String userId, @NonNull String roomId);
    void removeMemberFromRoom(@NonNull String userId, @NonNull String roomId);
    List<UserInRoom> getModerators(@NonNull String roomId);
    void addModeratorToRoom(@NonNull String userId, @NonNull String roomId);
    void addModeratorToRoomByUsername(@NonNull String username, @NonNull String roomId);
    void removeModeratorFromRoom(@NonNull String userId, @NonNull String roomId);
    boolean isModeratorOfRoom(@NonNull String userId, @NonNull String roomId);
    List<UserInRoom> getMembers(@NonNull String roomId, String keyword);

    List<String> getModeratorIds(@NonNull String roomId);
}
