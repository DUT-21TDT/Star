package com.pbl.star.events.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
@NoArgsConstructor
public class LikePostEvent {
    private String postId;
    private String actorId;
    private Instant timestamp;
}
