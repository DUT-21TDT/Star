package com.pbl.star.dtos.query.follow;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FollowSectionCount {
    private Integer followersCount;
    private Integer followingsCount;
    private Integer followRequestsCount;
}
