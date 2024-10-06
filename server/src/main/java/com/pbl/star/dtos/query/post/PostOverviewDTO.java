package com.pbl.star.dtos.query.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
public class PostOverviewDTO {
    private String id;
    private String username;
    private String userAvatarUrl;
    private Instant createdAt;
    private String content;
    private List<String> postImageUrls;
    private int numberOfLikes;
    private int numberOfComments;
    private int numberOfReposts;
    private boolean isLiked;
}
