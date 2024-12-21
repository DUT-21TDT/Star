package com.pbl.star.mapper.user;

import com.pbl.star.dtos.response.user.*;
import com.pbl.star.models.projections.user.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserDTOMapper {
    public abstract UserInRoomResponse toDTO(UserInRoom entity);
    public abstract OnDashboardProfileResponse toDTO(OnDashboardProfile entity);
    public abstract OnSuggestionProfileResponse toDTO(OnSuggestionProfile entity);
    public abstract OnSearchProfileResponse toDTO(OnSearchProfile entity);
    public abstract OnWallProfileResponse toDTO(OnWallProfile entity);
    public abstract OnFollowProfileResponse toDTO(OnFollowProfile entity);
    public abstract OnFollowReqProfileResponse toDTO(OnFollowRequestProfile entity);
    public abstract BasicUserInfoResponse toDTO(BasicUserInfo entity);
    public abstract DetailsUserInfoResponse toDTO(DetailsUserInfo entity);
    public abstract OnInteractProfileResponse toDTO(OnInteractProfile entity);
}
