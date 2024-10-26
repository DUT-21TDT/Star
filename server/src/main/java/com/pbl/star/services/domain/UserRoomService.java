package com.pbl.star.services.domain;

public interface UserRoomService {
    void addMemberToRoom(String userId, String roomId);
    void removeMemberFromRoom(String userId, String roomId);
    void addModeratorToRoom(String userId, String roomId);
    void removeModeratorFromRoom(String userId, String roomId);
    boolean isModeratorOfRoom(String userId, String roomId);
}
