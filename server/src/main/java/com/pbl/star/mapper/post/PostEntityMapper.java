package com.pbl.star.mapper.post;

import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.models.entities.Post;
import org.mapstruct.Mapper;

@Mapper
public abstract class PostEntityMapper {
    public abstract Post toEntity(CreatePostParams params);
}
