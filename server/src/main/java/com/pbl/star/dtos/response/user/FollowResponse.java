package com.pbl.star.dtos.response.user;

import com.pbl.star.enums.FollowStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FollowResponse {
    private String id;
    private FollowStatus followStatus;
}
