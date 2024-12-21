package com.pbl.star.models.projections.follow;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FollowCount {
    private Integer followersCount;
    private Integer followingsCount;
    private Integer followRequestsCount;
}
