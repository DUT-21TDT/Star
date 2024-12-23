package com.pbl.star.mapper.room;

import com.pbl.star.dtos.response.room.RoomDetailsForAdminResponse;
import com.pbl.star.dtos.response.room.RoomForAdminResponse;
import com.pbl.star.dtos.response.room.RoomForUserResponse;
import com.pbl.star.models.projections.room.RoomDetailsForAdmin;
import com.pbl.star.models.projections.room.RoomForAdmin;
import com.pbl.star.models.projections.room.RoomForUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class RoomDTOMapper {
    public abstract RoomForAdminResponse toDTO(RoomForAdmin room);

    @Mapping(source = "participant", target = "isParticipant")
    @Mapping(source = "moderator", target = "isModerator")
    public abstract RoomForUserResponse toDTO(RoomForUser room);
    public abstract RoomDetailsForAdminResponse toDTO(RoomDetailsForAdmin room);
}
