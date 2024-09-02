package com.pbl.star.mapper;

import com.pbl.star.dtos.request.room.CreateRoomParams;
import com.pbl.star.entities.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RoomCreationMapper {
    public abstract Room toEntity(CreateRoomParams params);
}
