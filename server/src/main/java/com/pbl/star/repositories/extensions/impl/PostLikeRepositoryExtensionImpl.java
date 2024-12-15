package com.pbl.star.repositories.extensions.impl;

import com.pbl.star.dtos.query.user.OnInteractProfile;
import com.pbl.star.enums.FollowStatus;
import com.pbl.star.enums.InteractType;
import com.pbl.star.repositories.extensions.PostLikeRepositoryExtension;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.time.Instant;
import java.util.List;

public class PostLikeRepositoryExtensionImpl implements PostLikeRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<OnInteractProfile> getPostLikes(String currentUserId, String postId, int limit, Instant after) {

        String sql = "SELECT u.user_id, u.username, u.avatar_url, u.first_name, u.last_name, pl.like_at, " +
                "(case " +
                "   when not exists (select 1 from following f where f.follower_id = :currentUserId and f.followee_id = u.user_id) then 'NOT_FOLLOWING' " +
                "   when exists (select 1 from following f where f.follower_id = :currentUserId and f.followee_id = u.user_id and f.status='ACCEPTED') then 'FOLLOWING' " +
                "   else 'REQUESTED' " +
                "end) " +
                "   as follow_status " +
                "FROM post_like pl " +
                "INNER JOIN \"user\" u " +
                "ON pl.user_id = u.user_id " +
                "WHERE pl.post_id = :postId " +
                (after != null ? "AND pl.like_at < :after " : "") +
                "ORDER BY pl.like_at desc, pl.post_like_id ";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("postId", postId)
                .setParameter("currentUserId", currentUserId)
                .setMaxResults(limit);

        if (after != null) {
            query.setParameter("after", after);
        }

        List<Object[]> results = query.getResultList();

        return results.stream().map(
                row -> OnInteractProfile.builder()
                        .userId((String) row[0])
                        .username((String) row[1])
                        .avatarUrl((String) row[2])
                        .firstName((String) row[3])
                        .lastName((String) row[4])
                        .interactType(InteractType.LIKE)
                        .interactAt((Instant) row[5])
                        .followStatus(FollowStatus.valueOf((String) row[6]))
                        .build()
        ).toList();
    }

    @Override
    public List<OnInteractProfile> getPostReposts(String currentUserId, String postId, int limit, Instant after) {
        String sql = "SELECT u.user_id, u.username, u.avatar_url, u.first_name, u.last_name, pr.repost_at, " +
                "(case " +
                "   when not exists (select 1 from following f where f.follower_id = :currentUserId and f.followee_id = u.user_id) then 'NOT_FOLLOWING' " +
                "   when exists (select 1 from following f where f.follower_id = :currentUserId and f.followee_id = u.user_id and f.status='ACCEPTED') then 'FOLLOWING' " +
                "   else 'REQUESTED' " +
                "end) " +
                "   as follow_status " +
                "FROM post_repost pr " +
                "INNER JOIN \"user\" u " +
                "ON pr.user_id = u.user_id " +
                "WHERE pr.post_id = :postId " +
                (after != null ? "AND pr.repost_at < :after " : "") +
                "ORDER BY pr.repost_at desc, pr.post_repost_id ";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("postId", postId)
                .setParameter("currentUserId", currentUserId)
                .setMaxResults(limit);

        if (after != null) {
            query.setParameter("after", after);
        }

        List<Object[]> results = query.getResultList();

        return results.stream().map(
                row -> OnInteractProfile.builder()
                        .userId((String) row[0])
                        .username((String) row[1])
                        .avatarUrl((String) row[2])
                        .firstName((String) row[3])
                        .lastName((String) row[4])
                        .interactType(InteractType.REPOST)
                        .interactAt((Instant) row[5])
                        .followStatus(FollowStatus.valueOf((String) row[6]))
                        .build()
        ).toList();
    }

    @Override
    public List<OnInteractProfile> getPostInteractions(String currentUserId, String postId, int limit, Instant after) {
        String sql =
                "(SELECT u.user_id, u.username, u.avatar_url, u.first_name, u.last_name, pl.like_at AS interact_at, " +
                        "       (CASE " +
                        "           WHEN NOT EXISTS (SELECT 1 FROM following f WHERE f.follower_id = :currentUserId AND f.followee_id = u.user_id) THEN 'NOT_FOLLOWING' " +
                        "           WHEN EXISTS (SELECT 1 FROM following f WHERE f.follower_id = :currentUserId AND f.followee_id = u.user_id AND f.status='ACCEPTED') THEN 'FOLLOWING' " +
                        "           ELSE 'REQUESTED' " +
                        "        END) AS follow_status, " +
                        "       'LIKE' AS interact_type " +
                        "FROM post_like pl " +
                        "INNER JOIN \"user\" u ON pl.user_id = u.user_id " +
                        "WHERE pl.post_id = :postId " +
                        (after != null ? "AND pl.like_at < :after " : "") +
                        "LIMIT :limit) " +

                        "UNION ALL " +

                        "(SELECT u.user_id, u.username, u.avatar_url, u.first_name, u.last_name, pr.repost_at AS interact_at, " +
                        "       (CASE " +
                        "           WHEN NOT EXISTS (SELECT 1 FROM following f WHERE f.follower_id = :currentUserId AND f.followee_id = u.user_id) THEN 'NOT_FOLLOWING' " +
                        "           WHEN EXISTS (SELECT 1 FROM following f WHERE f.follower_id = :currentUserId AND f.followee_id = u.user_id AND f.status='ACCEPTED') THEN 'FOLLOWING' " +
                        "           ELSE 'REQUESTED' " +
                        "        END) AS follow_status, " +
                        "       'REPOST' AS interact_type " +
                        "FROM post_repost pr " +
                        "INNER JOIN \"user\" u ON pr.user_id = u.user_id " +
                        "WHERE pr.post_id = :postId " +
                        (after != null ? "AND pr.repost_at < :after " : "") +
                        "LIMIT :limit) " +

                        "UNION ALL " +

                        "(SELECT u.user_id, u.username, u.avatar_url, u.first_name, u.last_name, p.created_at AS interact_at, " +
                        "       (CASE " +
                        "           WHEN NOT EXISTS (SELECT 1 FROM following f WHERE f.follower_id = :currentUserId AND f.followee_id = u.user_id) THEN 'NOT_FOLLOWING' " +
                        "           WHEN EXISTS (SELECT 1 FROM following f WHERE f.follower_id = :currentUserId AND f.followee_id = u.user_id AND f.status='ACCEPTED') THEN 'FOLLOWING' " +
                        "           ELSE 'REQUESTED' " +
                        "        END) AS follow_status, " +
                        "       'REPLY' AS interact_type " +
                        "FROM post p " +
                        "INNER JOIN \"user\" u ON p.user_id = u.user_id " +
                        "WHERE p.parent_post_id = :postId " +
                        (after != null ? "AND p.created_at < :after " : "") +
                        "LIMIT :limit) " +

                        "ORDER BY interact_at DESC, user_id ";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("postId", postId)
                .setParameter("currentUserId", currentUserId)
                .setParameter("limit", limit)
                .setMaxResults(limit);

        if (after != null) {
            query.setParameter("after", after);
        }

        List<Object[]> results = query.getResultList();

        return results.stream().map(
                row -> OnInteractProfile.builder()
                        .userId((String) row[0])
                        .username((String) row[1])
                        .avatarUrl((String) row[2])
                        .firstName((String) row[3])
                        .lastName((String) row[4])
                        .interactAt((Instant) row[5])
                        .followStatus(FollowStatus.valueOf((String) row[6]))
                        .interactType(InteractType.valueOf((String) row[7])) // LIKE, REPOST, REPLY
                        .build()
        ).toList();
    }
}
