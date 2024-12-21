package com.pbl.star.models.entities;

import com.pbl.star.utils.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;

@Entity
@Table(name = "notification_change")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationChange {
    @Id
    @GeneratedValue(generator = "ulid")
    @GenericGenerator(name = "ulid", type = IdGenerator.class)
    @Column(name = "notification_change_id")
    private String id;

    @Column(name = "notification_object_id")
    private String notificationObjectId;

    @Column(name = "actor_id")
    private String actorId;

    @Column(name = "change_at")
    private Instant changeAt;
}
