package com.pbl.star.dtos.query.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class RepostOnWallDTO {
    private PostForUserDTO repostedPost;
    private Instant repostedAt;
}
