package com.pbl.star.models.projections.notification;

import com.pbl.star.enums.ArtifactType;
import com.pbl.star.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class NotificationForUser {
    private String id;

    private NotificationType type;
    private String artifactId;
    private ArtifactType artifactType;
    private String artifactPreview;
    private boolean isRead;

    private NotificationActor lastActor;
    private Integer numberOfActors;

    private Instant changeAt;
}
