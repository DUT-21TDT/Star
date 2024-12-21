package com.pbl.star.mapper.room;

import com.pbl.star.dtos.request.room.CreateRoomParams;
import com.pbl.star.models.entities.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RoomEntityMapper {
    public abstract Room toEntity(CreateRoomParams params);
}
