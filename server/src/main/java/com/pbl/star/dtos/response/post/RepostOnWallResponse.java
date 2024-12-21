package com.pbl.star.dtos.response.post;

import com.pbl.star.models.projections.post.PostForUser;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class RepostOnWallResponse {
    private PostForUser repostedPost;
    private String repostedByUsername;
    private String caption;
    private Instant repostedAt;
}
