package com.pbl.star.dtos.query.notification;

import com.pbl.star.enums.ArtifactType;
import com.pbl.star.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class NotificationForUserDTO {
    private String id;

    private NotificationType type;
    private String artifactId;
    private ArtifactType artifactType;
    private boolean isRead;

    private NotificationActorDTO lastActor;
    private Integer numberOfActors;

    private Instant changeAt;
}
