package com.pbl.star.dtos.response.post;

import com.pbl.star.enums.PostStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
public class PostForModResponse {
    private String id;
    private String idOfCreator;
    private String idOfParentPost;
    private String usernameOfCreator;
    private String avatarUrlOfCreator;
    private String idOfRoom;
    private String nameOfRoom;
    private Instant createdAt;
    private String content;
    private List<String> postImageUrls;

    private PostStatus status;
    private Integer violenceScore;
    private String idOfModerator;
    private String usernameOfModerator;
    private Instant moderatedAt;
}
