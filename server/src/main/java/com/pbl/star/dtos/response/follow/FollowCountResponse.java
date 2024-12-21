package com.pbl.star.dtos.response.follow;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FollowCountResponse {
    private Integer followersCount;
    private Integer followingsCount;
    private Integer followRequestsCount;
}
