package com.pbl.star.dtos.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pbl.star.dtos.query.user.PublicProfile;
import com.pbl.star.enums.FollowStatus;
import lombok.*;

@Getter
@Setter
@Builder
public class PublicProfileResponse {
    private PublicProfile publicProfile;
    @JsonProperty("isCurrentUser")
    private boolean isCurrentUser;

    private FollowStatus followStatus;
}
