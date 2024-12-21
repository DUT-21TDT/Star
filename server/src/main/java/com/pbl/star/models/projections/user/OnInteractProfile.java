package com.pbl.star.models.projections.user;

import com.pbl.star.enums.FollowStatus;
import com.pbl.star.enums.InteractType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class OnInteractProfile {
    private String userId;
    private String username;
    private String avatarUrl;
    private String firstName;
    private String lastName;

    private InteractType interactType;
    private Instant interactAt;

    private FollowStatus followStatus;
}
