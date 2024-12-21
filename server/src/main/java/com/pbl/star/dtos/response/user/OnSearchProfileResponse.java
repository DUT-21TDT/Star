package com.pbl.star.dtos.response.user;

import com.pbl.star.enums.FollowStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OnSearchProfileResponse {
    private String userId;
    private String username;
    private String avatarUrl;
    private String firstName;
    private String lastName;
    // Metadata
    private Integer numberOfFollowers;
    private FollowStatus followStatus;
}
