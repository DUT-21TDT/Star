package com.pbl.star.models.projections.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class RepostOnWall {
    private PostForUser repostedPost;
    private String repostedByUsername;
    private String caption;
    private Instant repostedAt;
}
