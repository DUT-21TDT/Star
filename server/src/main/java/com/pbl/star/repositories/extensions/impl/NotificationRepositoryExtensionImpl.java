package com.pbl.star.repositories.extensions.impl;

import com.pbl.star.dtos.query.notification.NotificationActorDTO;
import com.pbl.star.dtos.query.notification.NotificationForUserDTO;
import com.pbl.star.enums.NotificationType;
import com.pbl.star.repositories.extensions.NotificationRepositoryExtension;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.Instant;
import java.util.List;

public class NotificationRepositoryExtensionImpl implements NotificationRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Slice<NotificationForUserDTO> getNotifications(Pageable pageable, Instant after, String userId) {

        String sql = "SELECT n.notification_id, " +
                "           nob.notification_type, nob.artifact_id, nob.is_read," +
                "           nc.actor_id, nc.created_at, " +
                "           (SELECT COUNT(*) FROM notification_change _nc WHERE _nc.notification_object_id = nob.notification_object_id)" +
                "           u.username, u.avatar_url " +
                "FROM notification n " +
                "INNER JOIN notification_object nob " +
                "ON n.notification_object_id = nob.notification_object_id " +
                "INNER JOIN ( " +
                "   SELECT _nc.actor_id, _nc.created_at, _nc.notification_object_id " +
                "   FROM notification_change _nc " +
                "   WHERE _nc.notification_object_id = nob.notification_object_id" +
                "   ORDER BY _nc.created_at desc " +
                "   LIMIT 1) nc " +
                "ON nob.notification_object_id = nc.notification_object_id " +
                "INNER JOIN \"user\" u " +
                "ON nc.actor_id = u.user_id " +
                "WHERE n.receiver_id = :userId " +
                (after != null ? "AND nc.created_at < :after " : "") +
                "ORDER BY nc.created_at desc, n.notification_id";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("userId", userId)
                .setMaxResults(pageable.getPageSize() + 1);

        if (after != null) {
            query.setParameter("after", after);
        }

        List<Object[]> resultList = query.getResultList();

        boolean hasNext = resultList.size() > pageable.getPageSize();

        if (hasNext) {
            resultList.removeLast();
        }

        List<NotificationForUserDTO> notifications = resultList.stream()
                .map(row -> NotificationForUserDTO.builder()
                        .id((String) row[0])
                        .type(NotificationType.valueOf((String) row[1]))
                        .artifactId((String) row[2])
                        .isRead((Boolean) row[3])
                        .numberOfActors((Integer) row[6])
                        .lastActor(NotificationActorDTO.builder()
                                .id((String) row[4])
                                .username((String) row[7])
                                .avatarUrl((String) row[8])
                                .build()
                        )
                        .createdAt((Instant) row[5])
                        .build()
                )
                .toList();

        return new SliceImpl<>(notifications, pageable, hasNext);
    }
}
