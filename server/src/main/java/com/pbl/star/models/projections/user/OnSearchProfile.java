package com.pbl.star.models.projections.user;

import com.pbl.star.enums.FollowStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OnSearchProfile {
    private String userId;
    private String username;
    private String avatarUrl;
    private String firstName;
    private String lastName;
    // Metadata
    private Integer numberOfFollowers;
    private FollowStatus followStatus;
}
