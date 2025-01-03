package com.pbl.star.models.projections.post;

import com.pbl.star.enums.PostStatus;
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
    protected String idOfCreator;
    private String idOfParentPost;
    protected String usernameOfCreator;
    protected String avatarUrlOfCreator;
    protected String idOfRoom;
    protected String nameOfRoom;
    protected Instant createdAt;
    protected String content;
    protected List<String> postImageUrls;
    private PostStatus status;
    private String rejectReason;
    private Boolean canModerate;
}
