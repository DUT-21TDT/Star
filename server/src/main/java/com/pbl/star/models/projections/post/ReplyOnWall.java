package com.pbl.star.models.projections.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReplyOnWall {
    private PostForUser parentPost;
    private PostForUser reply;
}
