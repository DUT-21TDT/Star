package com.pbl.star.repositories.extensions.impl;

import com.pbl.star.dtos.query.room.RoomForAdminDTO;
import com.pbl.star.dtos.query.room.RoomForUserDTO;
import com.pbl.star.repositories.extensions.RoomRepositoryExtension;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.time.Instant;
import java.util.List;

public class RoomRepositoryExtensionImpl implements RoomRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RoomForAdminDTO> getRoomsOverview() {
        String jpql = "SELECT r.id, r.name, r.description, r.createdAt, " +
                "(SELECT COUNT(*) " +
                "FROM UserRoom ur " +
                "where ur.roomId = r.id) AS participantsCount " +
                "FROM Room r";

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        return query.getResultList().stream()
                .map(row -> (RoomForAdminDTO) RoomForAdminDTO.builder()
                        .id((String) row[0])
                        .name((String) row[1])
                        .description((String) row[2])
                        .createdAt((Instant) row[3])
                        .participantsCount(((Long) row[4]).intValue())
                        .build())
                .toList();
    }

    @Override
    public List<RoomForUserDTO> getRoomsOverviewForUser(String userId) {

        String jpql = "SELECT r.id, r.name, r.description, r.createdAt, " +
                "(SELECT COUNT(urAll.userId) FROM UserRoom urAll WHERE urAll.roomId = r.id) AS participantsCount, " +
                "CASE " +
                "WHEN (SELECT ur.role FROM UserRoom ur WHERE ur.roomId = r.id AND ur.userId = :userId) = 'MEMBER' THEN 'MEMBER' " +
                "WHEN (SELECT ur.role FROM UserRoom ur WHERE ur.roomId = r.id AND ur.userId = :userId) = 'MODERATOR' THEN 'MODERATOR' " +
                "ELSE 'NOT_JOINED' " +
                "END AS joinRole " +
                "FROM Room r " +
                "LEFT JOIN UserRoom urAll ON urAll.roomId = r.id " +
                "LEFT JOIN UserRoom ur ON ur.roomId = r.id AND ur.userId = :userId " +
                "GROUP BY r.id, r.name, r.description, r.createdAt, joinRole";

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query.setParameter("userId", userId);

        return query.getResultList().stream()
                .map(row -> (RoomForUserDTO) RoomForUserDTO.builder()
                        .id((String) row[0])
                        .name((String) row[1])
                        .description((String) row[2])
                        .createdAt((Instant) row[3])
                        .participantsCount(((Long) row[4]).intValue())
                        .isParticipant(!"NOT_JOINED".equals(row[5]))
                        .isModerator("MODERATOR".equals(row[5]))
                        .build())
                .toList();
    }
}
