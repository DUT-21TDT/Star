package com.pbl.star.repositories.extensions.impl;

import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.query.user.PublicProfile;
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
    public PublicProfile getPublicProfile(String userId) {
        String jpql = "SELECT u.username, u.firstName, u.lastName, u.bio, u.avatarUrl, u.privateProfile " +
                "FROM User u " +
                "WHERE u.id = :userId and u.status = 'ACTIVE'";

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query.setParameter("userId", userId);

        try {
            Object[] result = query.getSingleResult();
            return PublicProfile.builder()
                    .username((String) result[0])
                    .firstName((String) result[1])
                    .lastName((String) result[2])
                    .bio((String) result[3])
                    .avatarUrl((String) result[4])
                    .privateProfile(result[5] != null && (boolean) result[5])
                    .numberOfFollowers(999) // TODO: Replace with actual follower count
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
