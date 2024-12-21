package com.pbl.star.mapper.notification;

import com.pbl.star.dtos.response.notification.NotificationForUserResponse;
import com.pbl.star.models.projections.notification.NotificationForUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class NotificationDTOMapper {
    @Mapping(source = "read", target = "isRead")
    public abstract NotificationForUserResponse toDTO(NotificationForUser entity);
}
