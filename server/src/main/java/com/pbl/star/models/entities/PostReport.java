package com.pbl.star.models.entities;

import com.pbl.star.enums.ReportStatus;
import com.pbl.star.utils.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;

@Entity
@Table(name = "post_report")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostReport {
    @Id
    @GeneratedValue(generator = "ulid")
    @GenericGenerator(name = "ulid", type = IdGenerator.class)
    @Column(name = "post_report_id")
    private String id;

    @Column(name = "post_id")
    private String postId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "report_at")
    private Instant reportAt;

    @Column(name = "reason")
    private String reason;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ReportStatus status;
}
