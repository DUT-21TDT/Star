package com.pbl.star.repositories.extensions.impl;

import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.query.user.PublicProfile;
import com.pbl.star.dtos.response.user.PublicProfileResponse;
import com.pbl.star.enums.FollowStatus;
import com.pbl.star.enums.Gender;
import com.pbl.star.repositories.extensions.UserRepositoryExtension;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.time.Instant;
import java.time.LocalDate;

public class UserRepositoryExtensionImpl implements UserRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PublicProfileResponse getPublicProfile(String currentId, String targetUserId) {

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
            PublicProfile publicProfile = PublicProfile.builder()
                    .username((String) result[0])
                    .firstName((String) result[1])
                    .lastName((String) result[2])
                    .bio((String) result[3])
                    .avatarUrl((String) result[4])
                    .privateProfile(result[5] != null && (boolean) result[5])
                    .numberOfFollowers(((Long) result[6]).intValue())
                    .build();

            return PublicProfileResponse.builder()
                    .publicProfile(publicProfile)
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
