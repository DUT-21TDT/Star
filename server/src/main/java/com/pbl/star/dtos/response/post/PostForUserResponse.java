package com.pbl.star.dtos.response.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
public class PostForUserResponse {
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

    private int numberOfLikes;
    private int numberOfComments;
    private int numberOfReposts;
    private boolean isLiked;
    private boolean isReposted;
}
