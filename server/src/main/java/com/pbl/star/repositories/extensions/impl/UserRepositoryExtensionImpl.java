package com.pbl.star.repositories.extensions.impl;

import com.pbl.star.dtos.query.user.OnSearchProfile;
import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.query.user.OnWallProfile;
import com.pbl.star.dtos.response.user.OnWallProfileResponse;
import com.pbl.star.enums.FollowStatus;
import com.pbl.star.enums.Gender;
import com.pbl.star.repositories.extensions.UserRepositoryExtension;
import jakarta.persistence.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class UserRepositoryExtensionImpl implements UserRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Slice<OnSearchProfile> searchUsers(Pageable pageable, String afterId, String currentUserId, String keyword) {
        String sql = getSearchUsersQuery(afterId);

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("keyword", keyword)
                .setParameter("currentId", currentUserId)
                .setFirstResult(0)
                .setMaxResults(pageable.getPageSize() + 1);

        if (afterId != null) {
            query.setParameter("afterId", afterId);
        }

        return getSliceOnSearchProfile(pageable, query);
    }

    private String getSearchUsersQuery(String afterId) {
        String sql = "select u.user_id, u.username, u.avatar_url, u.first_name, u.last_name, " +
                "(select count(*) from following f where f.followee_id = u.user_id and f.status='ACCEPTED') " +
                "   as follower_count, " +
                "(case " +
                "   when not exists (select 1 from Following f where f.follower_id = :currentId and f.followee_id = u.user_id) then 'NOT_FOLLOWING' " +
                "   when exists (select 1 from Following f where f.follower_id = :currentId and f.followee_id = u.user_id and f.status='ACCEPTED') then 'FOLLOWING' " +
                "   else 'REQUESTED' end) " +
                "   as follow_status " +
                "from \"user\" u " +
                "where username % :keyword or first_name % :keyword or last_name % :keyword " +
                "order by user_id ";

        if (afterId != null) {
            sql += "and user_id > :afterId ";
        }

        return sql;
    }

    private Slice<OnSearchProfile> getSliceOnSearchProfile(Pageable pageable, Query query) {
        List<Object[]> resultList = query.getResultList();

        boolean hasNext = resultList.size() > pageable.getPageSize();

        if (hasNext) {
            resultList.removeLast();
        }

        List<OnSearchProfile> content = resultList.stream()
                .map(row -> OnSearchProfile.builder()
                        .userId((String) row[0])
                        .username((String) row[1])
                        .avatarUrl((String) row[2])
                        .firstName((String) row[3])
                        .lastName((String) row[4])
                        .numberOfFollowers(((Long) row[5]).intValue())
                        .followStatus(FollowStatus.valueOf((String) row[6]))
                        .build())
                .toList();

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public OnWallProfileResponse getPublicProfile(String currentId, String targetUserId) {

        String jpql = "";
        TypedQuery<Object[]> query = null;
        boolean isCurrentUser = currentId.equals(targetUserId);

        if (isCurrentUser) {
            jpql = "SELECT u.username, u.firstName, u.lastName, u.bio, u.avatarUrl, u.privateProfile, " +
                    "(select count(*) from " +
                    "   Following f " +
                    "   where f.followeeId = u.id and f.status='ACCEPTED') " +
                    "   as follower_count " +
                    "FROM User u " +
                    "WHERE u.id = :userId and u.status = 'ACTIVE'";

            query = entityManager.createQuery(jpql, Object[].class);
            query.setParameter("userId", targetUserId);
        }

        else {
            jpql = "SELECT u.username, u.firstName, u.lastName, u.bio, u.avatarUrl, u.privateProfile, " +
                    "(select count(*) from " +
                    "   Following f " +
                    "   where f.followeeId = u.id and f.status='ACCEPTED') " +
                    "   as follower_count, " +
                    "(case " +
                    "   when not exists (select 1 from Following f where f.followerId = :currentId and f.followeeId = u.id) then 'NOT_FOLLOWING' " +
                    "   when exists (select 1 from Following f where f.followerId = :currentId and f.followeeId = u.id and f.status='ACCEPTED') then 'FOLLOWING' " +
                    "   else 'REQUESTED' end) " +
                    "   as follow_status " +
                    "FROM User u " +
                    "WHERE u.id = :userId and u.status = 'ACTIVE'";

            query = entityManager.createQuery(jpql, Object[].class);
            query.setParameter("userId", targetUserId);
            query.setParameter("currentId", currentId);
        }

        try {
            Object[] result = query.getSingleResult();
            OnWallProfile onWallProfile = OnWallProfile.builder()
                    .username((String) result[0])
                    .firstName((String) result[1])
                    .lastName((String) result[2])
                    .bio((String) result[3])
                    .avatarUrl((String) result[4])
                    .privateProfile(result[5] != null && (boolean) result[5])
                    .numberOfFollowers(((Long) result[6]).intValue())
                    .build();

            return OnWallProfileResponse.builder()
                    .publicProfile(onWallProfile)
                    .isCurrentUser(targetUserId.equals(currentId))
                    .followStatus(isCurrentUser ?
                            FollowStatus.NOT_FOLLOWING : FollowStatus.valueOf((String) result[7])
                    )
                    .build();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public PersonalInformation getPersonalInformation(String userId) {
        String jpql = "SELECT u.email, u.username, u.firstName, u.lastName, u.bio, u.avatarUrl, u.dateOfBirth, u.gender, u.registerAt, u.privateProfile " +
                "FROM User u " +
                "WHERE u.id = :userId and u.status = 'ACTIVE'";

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query.setParameter("userId", userId);

        try {
            Object[] result = query.getSingleResult();
            return PersonalInformation.builder()
                    .email((String) result[0])
                    .username((String) result[1])
                    .firstName((String) result[2])
                    .lastName((String) result[3])
                    .bio((String) result[4])
                    .avatarUrl((String) result[5])
                    .dateOfBirth((LocalDate) result[6])
                    .gender(result[7] == null ? null : (Gender) result[7])
                    .registerAt((Instant) result[8])
                    .privateProfile(result[9] != null && (boolean) result[9])
                    .build();
        } catch (NoResultException e) {
            return null;
        }
    }
}
