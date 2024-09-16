package com.pbl.star.services.impl;

import com.pbl.star.dtos.query.room.RoomOverviewDTO;
import com.pbl.star.dtos.request.room.CreateRoomParams;
import com.pbl.star.entities.Room;
import com.pbl.star.entities.User;
import com.pbl.star.entities.UserRoom;
import com.pbl.star.exceptions.EntityConflictException;
import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.exceptions.RequiredFieldMissingException;
import com.pbl.star.mapper.RoomCreationMapper;
import com.pbl.star.repositories.RoomRepository;
import com.pbl.star.repositories.UserRepository;
import com.pbl.star.repositories.UserRoomRepository;
import com.pbl.star.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UserRoomRepository userRoomRepository;

    @Override
    public List<RoomOverviewDTO> getRoomsOverview() {
        return roomRepository.getRoomsOverview();
    }

    @Override
    @Transactional
    public String createRoom(CreateRoomParams params) {

        if (StringUtils.isBlank(params.getName())) {
            throw new RequiredFieldMissingException("Room name cannot be empty");
        }

        if (roomRepository.existsByNameIgnoreCase(params.getName())) {
            throw new EntityConflictException("Room with name '" + params.getName() + "' already exists");
        }

        RoomCreationMapper roomCreationMapper = Mappers.getMapper(RoomCreationMapper.class);
        Room room = roomCreationMapper.toEntity(params);
        room.setCreatedAt(Instant.now());

        return roomRepository.save(room).getId();
    }

    @Override
    @Transactional
    public void deleteRoom(String roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new EntityNotFoundException("Room with id " + roomId + " does not exist");
        }

        roomRepository.deleteById(roomId);
    }

    @Override
    public void updateRoom(String roomId, CreateRoomParams params) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room with id '" + roomId + "' does not exist"));

        String newName = params.getName();
        String newDescription = params.getDescription();

        if (StringUtils.isBlank(newName)) {
            throw new RequiredFieldMissingException("Room name cannot be empty");
        }

        if (!newName.equalsIgnoreCase(room.getName()) && roomRepository.existsByNameIgnoreCase(newName)) {
            throw new EntityConflictException("Room with name '" + newName + "' already exists");
        }

        room.setName(newName);
        room.setDescription(newDescription);

        roomRepository.save(room);
    }

    @Override
    public void joinRoom(String userId, String roomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id '" + userId + "' does not exist"));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room with id '" + roomId + "' does not exist"));

        if (userRoomRepository.existsByUserAndRoom(user, room)) {
            throw new EntityConflictException("User is already a member of the room");
        }

        UserRoom userRoom = UserRoom.builder()
                .user(user)
                .room(room)
                .joinAt(Instant.now())
                .build();

        userRoomRepository.save(userRoom);
    }
}
