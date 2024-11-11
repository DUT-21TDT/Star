package com.pbl.star.repositories.extensions.impl;

import com.pbl.star.dtos.query.room.RoomDetailsForAdminDTO;
import com.pbl.star.dtos.query.room.RoomForAdminDTO;
import com.pbl.star.dtos.query.room.RoomForUserDTO;
import com.pbl.star.dtos.query.user.UserInRoom;
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
    public RoomDetailsForAdminDTO getRoomDetails(String roomId) {
        String jpql = "SELECT r.id, r.name, r.description, r.createdAt, " +
                "(SELECT COUNT(*) " +
                "FROM UserRoom ur " +
                "where ur.roomId = r.id) AS participantsCount, " +
                "(SELECT COUNT(*) " +
                "FROM Post p " +
                "WHERE p.roomId = r.id) as postsCount " +
                "FROM Room r " +
                "WHERE r.id = :roomId";

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query.setParameter("roomId", roomId);

        Object[] result = query.getSingleResult();

        if (result == null) {
            return null;
        }

        return RoomDetailsForAdminDTO.builder()
                .id((String) result[0])
                .name((String) result[1])
                .description((String) result[2])
                .createdAt((Instant) result[3])
                .participantsCount(((Long) result[4]).intValue())
                .postsCount(((Long) result[5]).intValue())
                .build();
    }

    @Override
    public List<UserInRoom> getModsInRoom(String roomId) {
        String jpql = "SELECT u.id, u.username, u.avatarUrl, u.firstName, u.lastName " +
                "FROM UserRoom ur " +
                "JOIN User u ON ur.userId = u.id " +
                "WHERE ur.roomId = :roomId and ur.role = 'MODERATOR' " +
                "ORDER BY u.username";

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query.setParameter("roomId", roomId);

        List<Object[]> result = query.getResultList();

        if (result.isEmpty()) {
            return List.of();
        }

        return result.stream().map(row -> UserInRoom.builder()
                .userId((String) row[0])
                .username((String) row[1])
                .avatarUrl((String) row[2])
                .firstName((String) row[3])
                .lastName((String) row[4])
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
