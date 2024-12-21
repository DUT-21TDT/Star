package com.pbl.star.models.entities;

import com.pbl.star.enums.FollowRequestStatus;
import com.pbl.star.utils.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;

@Entity
@Table(name = "following")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Following {
    @Id
    @GeneratedValue(generator = "ulid")
    @GenericGenerator(name = "ulid", type = IdGenerator.class)
    @Column(name = "following_id")
    private String id;

    @Column(name = "follower_id")
    private String followerId;

    @Column(name = "followee_id")
    private String followeeId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FollowRequestStatus status;

    @Column(name = "follow_at")
    private Instant followAt;
}
