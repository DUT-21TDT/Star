package com.pbl.star.models.projections.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotificationActor {
    private String id;
    private String username;
    private String avatarUrl;
}
