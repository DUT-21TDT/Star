package com.pbl.star.events.activity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ModeratePostEvent {
    private String postId;
    private Instant timestamp;
}
