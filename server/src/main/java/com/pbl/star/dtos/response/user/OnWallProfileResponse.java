package com.pbl.star.dtos.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pbl.star.dtos.query.user.OnWallProfile;
import com.pbl.star.enums.FollowStatus;
import lombok.*;

@Getter
@Setter
@Builder
public class OnWallProfileResponse {
    private OnWallProfile publicProfile;

    @JsonProperty("isCurrentUser")
    private boolean isCurrentUser;

    private FollowStatus followStatus;
}
