package com.pbl.star.dtos.response.post;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ReportForModResponse {
    private String reportId;
    private String postId;
    // User
    private String reporterId;
    private String reporterUsername;
    private String reporterAvatar;

    private String reason;
    private Instant reportAt;
}
