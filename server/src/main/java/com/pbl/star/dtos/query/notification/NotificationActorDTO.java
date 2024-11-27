package com.pbl.star.dtos.query.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotificationActorDTO {
    private String id;
    private String username;
    private String avatarUrl;
}
