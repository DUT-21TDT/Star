package com.pbl.star.events.activity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class NewPendingPostEvent {
    private String postId;
    private String actorId;
    private String roomId;
    private Instant timestamp;
}
