package com.pbl.star.mapper.follow;

import com.pbl.star.dtos.response.follow.FollowCountResponse;
import com.pbl.star.models.projections.follow.FollowCount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class FollowDTOMapper {
    public abstract FollowCountResponse toDTO(FollowCount followCount);
}
