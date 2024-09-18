package com.pbl.star.entities;

import com.pbl.star.utils.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;

@Entity
@Table(name = "user_room")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoom {
    @Id
    @GeneratedValue(generator = "ulid")
    @GenericGenerator(name = "ulid", type = IdGenerator.class)
    @Column(name = "user_room_id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "room_id")
    private String roomId;

    @Column(name = "join_at")
    private Instant joinAt;
}
