package com.pbl.star.mapper.room;

import com.pbl.star.dtos.response.room.RoomDetailsForAdminResponse;
import com.pbl.star.dtos.response.room.RoomForAdminResponse;
import com.pbl.star.dtos.response.room.RoomForUserResponse;
import com.pbl.star.models.projections.room.RoomDetailsForAdmin;
import com.pbl.star.models.projections.room.RoomForAdmin;
import com.pbl.star.models.projections.room.RoomForUser;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class RoomDTOMapper {
    public abstract RoomForAdminResponse toDTO(RoomForAdmin room);
    public abstract RoomForUserResponse toDTO(RoomForUser room);
    public abstract RoomDetailsForAdminResponse toDTO(RoomDetailsForAdmin room);
}
