package com.pbl.star.entities;

import com.pbl.star.utils.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;

@Entity
@Table(name = "room")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(generator = "ulid")
    @GenericGenerator(name = "ulid", type = IdGenerator.class)
    @Column(name = "room_id")
    private String id;

    private String name;
    private String description;

    @Column(name = "created_at")
    private Instant createdAt;
}
