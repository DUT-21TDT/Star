package com.pbl.star.models.entities;

import com.pbl.star.utils.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;

@Entity
@Table(name = "post_repost")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRepost {
    @Id
    @GeneratedValue(generator = "ulid")
    @GenericGenerator(name = "ulid", type = IdGenerator.class)
    @Column(name = "post_repost_id")
    private String id;

    @Column(name = "post_id")
    private String postId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "caption")
    private String caption;

    @Column(name = "repost_at")
    private Instant repostAt;
}
