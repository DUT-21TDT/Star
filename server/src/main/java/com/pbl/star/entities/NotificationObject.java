package com.pbl.star.entities;

import com.pbl.star.enums.ArtifactType;
import com.pbl.star.enums.NotificationType;
import com.pbl.star.utils.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "notification_object")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationObject {
    @Id
    @GeneratedValue(generator = "ulid")
    @GenericGenerator(name = "ulid", type = IdGenerator.class)
    @Column(name = "notification_object_id")
    private String id;

    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(name = "ref")
    private String ref;

    @Column(name = "artifact_id")
    private String artifactId;

    @Column(name = "artifact_type")
    @Enumerated(EnumType.STRING)
    private ArtifactType artifactType;

    @Column(name = "artifact_preview")
    private String artifactPreview;

    @Column(name = "is_read")
    private boolean isRead;
}
