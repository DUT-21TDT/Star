package com.pbl.star.services.domain.impl;

import com.pbl.star.dtos.query.user.UserInRoom;
import com.pbl.star.entities.User;
import com.pbl.star.entities.UserRoom;
import com.pbl.star.enums.RoomRole;
import com.pbl.star.exceptions.EntityConflictException;
import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.repositories.RoomRepository;
import com.pbl.star.repositories.UserRepository;
import com.pbl.star.repositories.UserRoomRepository;
import com.pbl.star.services.domain.UserRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoomServiceImpl implements UserRoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UserRoomRepository userRoomRepository;

    @Override
    @Transactional
    public void addMemberToRoom(@NonNull String userId,
                                @NonNull String roomId) {

        if (!roomRepository.existsById(roomId)) {
            throw new EntityNotFoundException("Room with id '" + roomId + "' does not exist");
        }

        if (userRoomRepository.existsByUserIdAndRoomId(userId, roomId)) {
            throw new EntityConflictException("User is already a member of the room");
        }

        UserRoom userRoom = UserRoom.builder()
                .userId(userId)
                .roomId(roomId)
                .role(RoomRole.MEMBER)
                .joinAt(Instant.now())
                .build();

        userRoomRepository.save(userRoom);
    }

    @Override
    @Transactional
    public void removeMemberFromRoom(@NonNull String userId,
                                     @NonNull String roomId) {

        UserRoom userRoom = userRoomRepository.findByUserIdAndRoomId(userId, roomId)
                .orElseThrow(() -> new EntityNotFoundException("User is not a member of the room"));

        userRoomRepository.delete(userRoom);
    }

    @Override
    public List<UserInRoom> getModerators(@NonNull String roomId) {
        return roomRepository.getModsInRoom(roomId);
    }

    @Override
    @Transactional
    public void addModeratorToRoom(@NonNull String userId,
                                   @NonNull String roomId) {

        if (!roomRepository.existsById(roomId)) {
            throw new EntityNotFoundException("Room with id '" + roomId + "' does not exist");
        }

        UserRoom userRoom = userRoomRepository.findByUserIdAndRoomId(userId, roomId)
                .orElseThrow(() -> new EntityNotFoundException("User is not a member of the room"));

        if (userRoom.getRole() == RoomRole.MODERATOR) {
            throw new EntityConflictException("User is already a moderator of the room");
        }

        userRoom.setRole(RoomRole.MODERATOR);
        userRoomRepository.save(userRoom);
    }

    @Override
    @Transactional
    public void addModeratorToRoomByUsername(@NonNull String username,
                                             @NonNull String roomId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username '" + username + "' not found"));

        addModeratorToRoom(user.getId(), roomId);
    }

    @Override
    @Transactional
    public void removeModeratorFromRoom(@NonNull String userId,
                                        @NonNull String roomId) {

        UserRoom userRoom = userRoomRepository.findByUserIdAndRoomId(userId, roomId)
                .orElseThrow(() -> new EntityNotFoundException("User is not a member of the room"));

        if (userRoom.getRole() == RoomRole.MEMBER) {
            throw new EntityConflictException("User is not a moderator of the room");
        }

        userRoom.setRole(RoomRole.MEMBER);
        userRoomRepository.save(userRoom);
    }

    @Override
    public boolean isModeratorOfRoom(@NonNull String userId,
                                     @NonNull String roomId) {

        return userRoomRepository.existsByUserIdAndRoomIdAndRole(userId, roomId, RoomRole.MODERATOR);
    }

    @Override
    public List<UserInRoom> getMembers(@NonNull String roomId, String keyword) {
        return userRepository.findUsersInRoom(roomId, keyword);
    }
}
