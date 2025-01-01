package com.pbl.star.repositories.extensions;

import com.pbl.star.models.projections.report.ReportForMod;

import java.time.Instant;
import java.util.List;

public interface PostReportRepositoryExtension {
    List<ReportForMod> getReportsOfPost(String postId, int limit, Instant after);
}
