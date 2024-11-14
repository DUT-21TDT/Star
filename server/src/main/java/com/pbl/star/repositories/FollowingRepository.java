package com.pbl.star.repositories;

import com.pbl.star.entities.Following;
import com.pbl.star.enums.FollowRequestStatus;
import com.pbl.star.repositories.extensions.FollowingRepositoryExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FollowingRepository extends JpaRepository<Following, String>, FollowingRepositoryExtension {
    Optional<Following> findByFollowerIdAndFolloweeId(String followerId, String followeeId);

    @Query("SELECT CASE WHEN EXISTS ( " +
            "SELECT 1 FROM Following f WHERE f.followerId=?1 and f.followeeId=?2 AND f.status='ACCEPTED') " +
            "THEN TRUE ELSE FALSE END")
    boolean isFollowing(String followerId, String followeeId);
    boolean existsByFollowerIdAndFolloweeId(String followerId, String followeeId);

    @Query("UPDATE Following f SET f.status='ACCEPTED' WHERE f.followeeId=?1 AND f.status='PENDING'")
    void updateAllPendingRequestsToAccepted(String followeeId);

    Long countByFollowerIdAndStatus(String followerId, FollowRequestStatus status);
    Long countByFolloweeIdAndStatus(String followeeId, FollowRequestStatus status);
}
