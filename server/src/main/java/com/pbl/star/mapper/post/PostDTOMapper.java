package com.pbl.star.mapper.post;

import com.pbl.star.dtos.response.post.*;
import com.pbl.star.models.projections.post.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class PostDTOMapper {
    @Mapping(source = "liked", target = "isLiked")
    @Mapping(source = "reposted", target = "isReposted")
    public abstract PostForUserResponse toDTO(PostForUser post);
    public abstract ReplyOnWallResponse toDTO(ReplyOnWall post);
    public abstract RepostOnWallResponse toDTO(RepostOnWall post);
    public abstract PendingPostForUserResponse toDTO(PendingPostForUser post);
    public abstract PostForModResponse toDTO(PostForMod post);
    @Mapping(source = "hidden", target = "isHidden")
    public abstract PostForAdminResponse toDTO(PostForAdmin post);
}
