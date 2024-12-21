package com.pbl.star.dtos.response.user;

import com.pbl.star.enums.FollowStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class OnFollowProfileResponse {
    private String userId;
    private String username;
    private String avatarUrl;
    private String firstName;
    private String lastName;
    // Metadata
    private Instant followAt;
    private FollowStatus followStatus;
}
