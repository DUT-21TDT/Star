package com.pbl.star.models.entities;

import com.pbl.star.enums.PostStatus;
import com.pbl.star.utils.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;

@Entity
@Table(name = "post")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(generator = "ulid")
    @GenericGenerator(name = "ulid", type = IdGenerator.class)
    @Column(name = "post_id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "room_id")
    private String roomId;

    @Column(name = "parent_post_id")
    private String parentPostId;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @Column(name = "violence_score")
    private Integer violenceScore;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "moderated_by")
    private String moderatedBy;

    @Column(name = "moderated_at")
    private Instant moderatedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "is_hidden")
    private boolean isHidden;

    @Column(name = "hide_at")
    private Instant hideAt;
}
