package com.pbl.star.dtos.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pbl.star.enums.FollowStatus;
import com.pbl.star.models.projections.user.OnWallProfileUser;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OnWallProfileResponse {
    private OnWallProfileUser publicProfile;

    @JsonProperty("isCurrentUser")
    private boolean isCurrentUser;

    private FollowStatus followStatus;
}
