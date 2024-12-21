package com.pbl.star.dtos.response.user;

import com.pbl.star.enums.FollowStatus;
import com.pbl.star.enums.InteractType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class OnInteractProfileResponse {
    private String userId;
    private String username;
    private String avatarUrl;
    private String firstName;
    private String lastName;

    private InteractType interactType;
    private Instant interactAt;

    private FollowStatus followStatus;
}
