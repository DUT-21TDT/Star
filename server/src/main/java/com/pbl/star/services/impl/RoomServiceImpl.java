package com.pbl.star.services.impl;

import com.pbl.star.dtos.request.room.CreateRoomParams;
import com.pbl.star.entities.Room;
import com.pbl.star.exceptions.EntityConflictException;
import com.pbl.star.exceptions.RequiredFieldMissingException;
import com.pbl.star.mapper.RoomCreationMapper;
import com.pbl.star.repositories.RoomRepository;
import com.pbl.star.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    @Transactional
    public String createRoom(CreateRoomParams params) {

        if (params.getName() == null || params.getName().isEmpty()) {
            throw new RequiredFieldMissingException("Room name cannot be empty");
        }

        if (roomRepository.existsByNameIgnoreCase(params.getName())) {
            throw new EntityConflictException("Room with name " + params.getName() + " already exists");
        }

        RoomCreationMapper roomCreationMapper = Mappers.getMapper(RoomCreationMapper.class);
        Room room = roomCreationMapper.toEntity(params);
        room.setCreatedAt(Instant.now());

        return roomRepository.save(room).getId();
    }
}
