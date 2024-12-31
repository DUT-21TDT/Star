package com.pbl.star.models.projections.post;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class PostForUser extends PostGeneral {
    private int numberOfLikes;
    private int numberOfComments;
    private int numberOfReposts;
    private boolean isLiked;
    private boolean isReposted;
}
