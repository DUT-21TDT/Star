package com.pbl.star.repositories;

import com.pbl.star.models.entities.PostReport;
import com.pbl.star.repositories.extensions.PostReportRepositoryExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostReportRepository extends JpaRepository<PostReport, String>, PostReportRepositoryExtension {
    Optional<PostReport> findByPostIdAndUserId(String postId, String userId);
    boolean existsByPostIdAndUserId(String postId, String userId);
}
