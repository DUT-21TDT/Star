package com.pbl.star.entities;

import com.pbl.star.utils.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "notification")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(generator = "ulid")
    @GenericGenerator(name = "ulid", type = IdGenerator.class)
    @Column(name = "notification_id")
    private String id;

    @Column(name = "notification_object_id")
    private String notificationObjectId;

    @Column(name = "receiver_id")
    private String receiverId;


}
