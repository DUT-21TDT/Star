package com.pbl.star.repositories.extensions.impl;

import com.pbl.star.dtos.query.notification.NotificationActorDTO;
import com.pbl.star.dtos.query.notification.NotificationForUserDTO;
import com.pbl.star.enums.ArtifactType;
import com.pbl.star.enums.NotificationType;
import com.pbl.star.repositories.extensions.NotificationRepositoryExtension;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;


import java.time.Instant;
import java.util.List;

public class NotificationRepositoryExtensionImpl implements NotificationRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<NotificationForUserDTO> getNotifications(int limit, Instant after, String userId) {

        String sql = "SELECT n.notification_id, " +
                "           nob.notification_type, nob.artifact_id, nob.artifact_type, nob.is_read," +
                "           nc.actor_id, nc.change_at, " +
                "           (SELECT COUNT(distinct _nc.actor_id) FROM notification_change _nc WHERE _nc.notification_object_id = nob.notification_object_id), " +
                "           u.username, u.avatar_url, nob.artifact_preview " +
                "FROM notification n " +
                "INNER JOIN notification_object nob " +
                "ON n.notification_object_id = nob.notification_object_id " +
                "INNER JOIN ( " +
                "   SELECT _nc.actor_id, _nc.change_at, _nc.notification_object_id " +
                "   FROM (" +
                "       SELECT _nc.actor_id, _nc.change_at, _nc.notification_object_id, " +
                "              ROW_NUMBER() OVER (PARTITION BY _nc.notification_object_id ORDER BY _nc.change_at desc) as rn " +
                "       FROM notification_change _nc " +
                "   ) _nc " +
                "   WHERE _nc.rn = 1" +
                ") nc " +
                "ON nob.notification_object_id = nc.notification_object_id " +
                "INNER JOIN \"user\" u " +
                "ON nc.actor_id = u.user_id " +
                "WHERE n.receiver_id = :userId " +
                (after != null ? "AND nc.change_at < :after " : "") +
                "ORDER BY nc.change_at desc, n.notification_id";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("userId", userId)
                .setMaxResults(limit);

        if (after != null) {
            query.setParameter("after", after);
        }

        List<Object[]> resultList = query.getResultList();

        return resultList.stream()
                .map(row -> NotificationForUserDTO.builder()
                        .id((String) row[0])
                        .type(NotificationType.valueOf((String) row[1]))
                        .artifactId((String) row[2])
                        .artifactType(ArtifactType.valueOf((String) row[3]))
                        .isRead((Boolean) row[4])
                        .numberOfActors(((Long) row[7]).intValue())
                        .lastActor(NotificationActorDTO.builder()
                                .id((String) row[5])
                                .username((String) row[8])
                                .avatarUrl((String) row[9])
                                .build()
                        )
                        .changeAt((Instant) row[6])
                        .artifactPreview((String) row[10])
                        .build()
                )
                .toList();
    }
}
