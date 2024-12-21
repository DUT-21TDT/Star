package com.pbl.star.models.projections.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pbl.star.enums.FollowStatus;
import lombok.*;

@Getter
@Setter
@Builder
public class OnWallProfile {
    private OnWallProfileUser publicProfile;

    @JsonProperty("isCurrentUser")
    private boolean isCurrentUser;

    private FollowStatus followStatus;
}
