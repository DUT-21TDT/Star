package com.pbl.star.repositories.extensions.impl;

import com.pbl.star.dtos.query.user.OnFollowProfile;
import com.pbl.star.enums.FollowStatus;
import com.pbl.star.repositories.extensions.FollowingRepositoryExtension;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.List;

public class FollowingRepositoryExtensionImpl implements FollowingRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Slice<OnFollowProfile> getFollowingsOfUser(Pageable pageable, Instant after, String currentUserId, String targetUserId) {

        String jpql;

        if (currentUserId.equals(targetUserId)) {
            jpql = "SELECT u.id, u.username, u.avatarUrl, u.firstName, u.lastName, 'FOLLOWING' as follow_status, f.followAt " +
                    "FROM Following f " +
                    "INNER JOIN User u " +
                    "ON f.followeeId = u.id " +
                    "WHERE f.followerId = :targetUserId " +
                    "AND f.status = 'ACCEPTED' " +
                    (after != null ? "AND f.followAt < :after " : "") +
                    "ORDER BY f.followAt DESC, f.id";
        } else {
            jpql = "SELECT u.id, u.username, u.avatarUrl, u.firstName, u.lastName, " +
                    "(case " +
                    "   when not exists (select 1 from Following f where f.followerId = :currentId and f.followeeId = u.id) then 'NOT_FOLLOWING' " +
                    "   when exists (select 1 from Following f where f.followerId = :currentId and f.followeeId = u.id and f.status='ACCEPTED') then 'FOLLOWING' " +
                    "   else 'REQUESTED' end) " +
                    "   as follow_status, " +
                    "f.followAt " +
                    "FROM Following f " +
                    "INNER JOIN User u " +
                    "ON f.followeeId = u.id " +
                    "WHERE f.followerId = :targetUserId " +
                    "AND f.status = 'ACCEPTED' " +
                    (after != null ? "AND f.followAt < :after " : "") +
                    "ORDER BY f.followAt DESC, f.id";
        }

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);

        if (!currentUserId.equals(targetUserId)) {
            query.setParameter("currentId", currentUserId);
        }

        query.setParameter("targetUserId", targetUserId)
                .setMaxResults(pageable.getPageSize() + 1);

        if (after != null) {
            query.setParameter("after", after);
        }

        return toOnFollowProfileSlice(query, pageable);
    }

    @Override
    public Slice<OnFollowProfile> getFollowersOfUser(Pageable pageable, Instant after, String currentUserId, String targetUserId) {

        String jpql = "SELECT u.id, u.username, u.avatarUrl, u.firstName, u.lastName, " +
                "(case " +
                "   when not exists (select 1 from Following f where f.followerId = :currentId and f.followeeId = u.id) then 'NOT_FOLLOWING' " +
                "   when exists (select 1 from Following f where f.followerId = :currentId and f.followeeId = u.id and f.status='ACCEPTED') then 'FOLLOWING' " +
                "   else 'REQUESTED' end) " +
                "   as follow_status, " +
                "f.followAt " +
                "FROM Following f " +
                "INNER JOIN User u " +
                "ON f.followerId = u.id " +
                "WHERE f.followeeId = :targetUserId " +
                "AND f.status = 'ACCEPTED' " +
                (after != null ? "AND f.followAt < :after " : "") +
                "ORDER BY f.followAt DESC, f.id";

        return getOnFollowProfiles(pageable, after, currentUserId, targetUserId, jpql);
    }

    @NonNull
    private Slice<OnFollowProfile> getOnFollowProfiles(Pageable pageable, Instant after, String currentUserId, String targetUserId, String jpql) {
        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);

        query.setParameter("currentId", currentUserId)
                .setParameter("targetUserId", targetUserId)
                .setMaxResults(pageable.getPageSize() + 1);

        if (after != null) {
            query.setParameter("after", after);
        }

        return toOnFollowProfileSlice(query, pageable);
    }

    private Slice<OnFollowProfile> toOnFollowProfileSlice(TypedQuery<Object[]> query, Pageable pageable) {

        List<Object[]> resultList = query.getResultList();
        boolean hasNext = resultList.size() > pageable.getPageSize();

        if (hasNext) {
            resultList.removeLast();
        }

        List<OnFollowProfile> content = resultList.stream()
                .map(row -> OnFollowProfile.builder()
                        .userId((String) row[0])
                        .username((String) row[1])
                        .avatarUrl((String) row[2])
                        .firstName((String) row[3])
                        .lastName((String) row[4])
                        .followStatus(FollowStatus.valueOf((String) row[5]))
                        .followAt((Instant) row[6])
                        .build())
                .toList();

        return new SliceImpl<>(content, pageable, hasNext);
    }
}
