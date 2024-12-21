package com.pbl.star.services.domain.impl;

import com.pbl.star.models.projections.room.RoomDetailsForAdmin;
import com.pbl.star.models.projections.room.RoomForAdmin;
import com.pbl.star.models.projections.room.RoomForUser;
import com.pbl.star.dtos.request.room.CreateRoomParams;
import com.pbl.star.models.entities.Room;
import com.pbl.star.exceptions.EntityConflictException;
import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.mapper.room.RoomEntityMapper;
import com.pbl.star.repositories.RoomRepository;
import com.pbl.star.services.domain.RoomService;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public RoomDetailsForAdmin getRoomDetails(String roomId) {
        RoomDetailsForAdmin roomDetails = roomRepository.getRoomDetails(roomId);

        if (roomDetails == null) {
            throw new EntityNotFoundException("Room with id " + roomId + " does not exist");
        }

        return roomDetails;
    }

    @Override
    public List<RoomForAdmin> getRoomsOverview() {
        return roomRepository.getRoomsOverview();
    }

    @Override
    public List<RoomForUser> getRoomsOverviewForUser() {
        String userId = AuthUtil.getCurrentUser().getId();
        return roomRepository.getRoomsOverviewForUser(userId);
    }

    @Override
    @Transactional
    public Room createRoom(CreateRoomParams params) {

        if (roomRepository.existsByNameIgnoreCase(params.getName())) {
            throw new EntityConflictException("Room with name '" + params.getName() + "' already exists");
        }

        RoomEntityMapper roomCreationMapper = Mappers.getMapper(RoomEntityMapper.class);
        Room room = roomCreationMapper.toEntity(params);
        room.setCreatedAt(Instant.now());

        return roomRepository.save(room);
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
    @Transactional
    public void updateRoom(String roomId, CreateRoomParams params) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room with id '" + roomId + "' does not exist"));

        String newName = params.getName();
        String newDescription = params.getDescription();

        if (!newName.equalsIgnoreCase(room.getName()) && roomRepository.existsByNameIgnoreCase(newName)) {
            throw new EntityConflictException("Room with name '" + newName + "' already exists");
        }

        room.setName(newName);
        room.setDescription(newDescription);

        roomRepository.save(room);
    }
}
