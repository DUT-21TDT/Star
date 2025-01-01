package com.pbl.star.models.projections.report;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class ReportForMod {
    private String reportId;
    private String postId;
    // User
    private String reporterId;
    private String reporterUsername;
    private String reporterAvatar;

    private String reason;
    private Instant reportAt;
}
