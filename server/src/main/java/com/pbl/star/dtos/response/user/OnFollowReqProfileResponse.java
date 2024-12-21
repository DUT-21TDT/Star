package com.pbl.star.dtos.response.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class OnFollowReqProfileResponse {
    private String userId;
    private String username;
    private String avatarUrl;
    private String firstName;
    private String lastName;
    // Metadata
    private String followingId;
    private Instant followAt;
}

