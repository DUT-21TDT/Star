package com.pbl.star.repositories.extensions.impl;

import com.pbl.star.dtos.query.room.RoomOverviewDTO;
import com.pbl.star.dtos.query.room.RoomOverviewForUserDTO;
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
    public List<RoomOverviewDTO> getRoomsOverview() {
        String jpql = "SELECT r.id, r.name, r.description, r.createdAt, " +
                "(SELECT COUNT(*) " +
                "FROM UserRoom ur " +
                "where ur.roomId = r.id) AS participantsCount " +
                "FROM Room r";

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        return query.getResultList().stream()
                .map(row -> new RoomOverviewDTO(
                        (String) row[0],
                        (String) row[1],
                        (String) row[2],
                        (Instant) row[3],
                        ((Long) row[4]).intValue()
                ))
                .toList();
    }

    @Override
    public List<RoomOverviewForUserDTO> getRoomsOverviewForUser(String userId) {
        String jpql = "SELECT r.id, r.name, r.description, r.createdAt, " +
                "(SELECT COUNT(*) FROM UserRoom ur WHERE ur.roomId = r.id) AS participantsCount, " +
                "CASE WHEN (SELECT COUNT(*) FROM UserRoom ur WHERE ur.roomId = r.id AND ur.userId = :userId) > 0 THEN TRUE ELSE FALSE END AS isParticipant " +
                "FROM Room r";

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query.setParameter("userId", userId);

        return query.getResultList().stream()
                .map(row -> RoomOverviewForUserDTO.builder()
                        .id((String) row[0])
                        .name((String) row[1])
                        .description((String) row[2])
                        .createdAt((Instant) row[3])
                        .participantsCount(((Long) row[4]).intValue())
                        .isParticipant((boolean) row[5])
                        .build())
                .toList();
    }
}
