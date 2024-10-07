package com.pbl.star.mapper;

import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.entities.Post;
import org.mapstruct.Mapper;

@Mapper
public abstract class PostCreationMapper {
    public abstract Post toEntity(CreatePostParams params);
}
