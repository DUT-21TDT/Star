package com.pbl.star.models.projections.user;

import com.pbl.star.enums.FollowStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OnWallProfile {
    private OnWallProfileUser publicProfile;

    private boolean isCurrentUser;
    private FollowStatus followStatus;
}
