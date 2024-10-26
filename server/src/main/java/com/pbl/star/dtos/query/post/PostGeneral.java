package com.pbl.star.dtos.query.post;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@SuperBuilder
abstract class PostGeneral {
    protected String id;
    protected String usernameOfCreator;
    protected String avatarUrlOfCreator;
    protected String idOfCreator;
    protected Instant createdAt;
    protected String content;
    protected List<String> postImageUrls;
}
