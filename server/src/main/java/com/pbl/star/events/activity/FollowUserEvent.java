package com.pbl.star.events.activity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class FollowUserEvent {
    private String followingId;
    private String followeeId;
    private String followerId;
    private Instant timestamp;
}
