package com.pbl.star.dtos.query.post;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public final class PostForUserDTO extends PostGeneral {
    private int numberOfLikes;
    private int numberOfComments;
    private int numberOfReposts;
    private boolean isLiked;
    private boolean isReposted;
}
