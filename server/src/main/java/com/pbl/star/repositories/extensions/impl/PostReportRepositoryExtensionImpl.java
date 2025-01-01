package com.pbl.star.repositories.extensions.impl;

import com.pbl.star.models.projections.report.ReportForMod;
import com.pbl.star.repositories.extensions.PostReportRepositoryExtension;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.time.Instant;
import java.util.List;

public class PostReportRepositoryExtensionImpl implements PostReportRepositoryExtension {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ReportForMod> getReportsOfPost(String postId, int limit, Instant after) {

        String sql = "SELECT pr.post_report_id, pr.post_id, pr.user_id, u.username, u.avatar_url, pr.reason, pr.report_at " +
                "FROM post_report pr " +
                "INNER JOIN \"user\" u " +
                "ON pr.user_id = u.user_id " +
                "WHERE pr.post_id = :postId " +
                (after != null ? "AND pr.report_at < :after " : "") +
                "ORDER BY pr.report_at DESC, pr.post_report_id";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("postId", postId)
                .setMaxResults(limit);

        if (after != null) {
            query.setParameter("after", after);
        }

        List<Object[]> resultList = query.getResultList();
        return resultList.stream().map(
                row -> ReportForMod.builder()
                        .reportId((String) row[0])
                        .postId((String) row[1])
                        .reporterId((String) row[2])
                        .reporterUsername((String) row[3])
                        .reporterAvatar((String) row[4])
                        .reason((String) row[5])
                        .reportAt((Instant) row[6])
                        .build()
        ).toList();
    }
}
