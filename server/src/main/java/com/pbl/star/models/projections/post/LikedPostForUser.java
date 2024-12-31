package com.pbl.star.models.projections.post;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@SuperBuilder
public class LikedPostForUser extends PostForUser {
    private Instant likedAt;
}
