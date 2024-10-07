package com.pbl.star.dtos.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pbl.star.dtos.query.user.PublicProfile;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicProfileResponse {
    private PublicProfile publicProfile;
    @JsonProperty("isCurrentUser")
    private boolean isCurrentUser;
    @JsonProperty("isFollowing")
    private boolean isFollowing;
}
