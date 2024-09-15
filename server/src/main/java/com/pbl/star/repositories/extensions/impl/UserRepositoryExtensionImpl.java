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
    public PublicProfile getPublicProfile(String username) {
        String jpql = "SELECT u.username, u.firstName, u.lastName, u.bio, u.avatarUrl, u.privateProfile " +
                "FROM User u " +
                "WHERE u.username = :username and u.status = 'ACTIVE'";

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query.setParameter("username", username);

        try {
            Object[] result = query.getSingleResult();
            return new PublicProfile(
                    (String) result[0],
                    (String) result[1],
                    (String) result[2],
                    (String) result[3],
                    (String) result[4],
                    (boolean) result[5],
                    // TODO: Replace with actual follower count
                    999
            );
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
            return new PersonalInformation(
                    (String) result[0],
                    (String) result[1],
                    (String) result[2],
                    (String) result[3],
                    (String) result[4],
                    (String) result[5],
                    (LocalDate) result[6],
                    (result[7] == null ? null : Gender.valueOf((String) result[7])),
                    (Instant) result[8],
                    (Boolean) result[9]
            );
        } catch (NoResultException e) {
            return null;
        }
    }
}
