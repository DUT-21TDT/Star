package com.pbl.star.repositories.extensions.impl;

import com.pbl.star.dtos.query.room.RoomOverviewDTO;
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
        String jpql = "SELECT r.id, r.name, r.description, r.createdAt, COUNT(ur.user.id) AS participantsCount " +
                "FROM Room r " +
                "LEFT JOIN UserRoom ur ON r.id = ur.room.id " +
                "GROUP BY r.id";

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
}
