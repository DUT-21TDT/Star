package com.pbl.star.repositories.extensions.impl;

import com.pbl.star.dtos.query.post.PostOverviewDTO;
import com.pbl.star.enums.PostStatus;
import com.pbl.star.repositories.extensions.PostRepositoryExtension;
import com.pbl.star.utils.AuthUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.List;

public class PostRepositoryExtensionImpl implements PostRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Slice<PostOverviewDTO> findPostOverviewsByStatusAndUser(Pageable pageable, Instant after, PostStatus status, String userId) {

        String currentUserId = AuthUtil.getCurrentUser().getId();

        String jpql = getPostOverviewQuery(ConditionType.USER, after, status);

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query
                .setParameter("userId", userId)
                .setParameter("currentUserId", currentUserId)
                .setParameter("status", status)
                .setFirstResult(0)
                .setMaxResults(pageable.getPageSize() + 1);

        if (after != null) {
            query.setParameter("after", after);
        }

        return getSlicePostOverviewDTOS(pageable, query);
    }

    @Override
    public Slice<PostOverviewDTO> findPostOverviewsByStatusInRooms(Pageable pageable, Instant after, PostStatus
            status, String... roomIds) {
        String currentUserId = AuthUtil.getCurrentUser().getId();

        String jpql = getPostOverviewQuery(ConditionType.ROOM, after, status);

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query
                .setParameter("roomIds", List.of(roomIds))
                .setParameter("currentUserId", currentUserId)
                .setParameter("status", status)
                .setFirstResult(0)
                .setMaxResults(pageable.getPageSize() + 1);

        if (after != null) {
            query.setParameter("after", after);
        }

        return getSlicePostOverviewDTOS(pageable, query);
    }

    private static String getPostOverviewQuery(ConditionType type, Instant after, PostStatus status) {
        String jpql = "SELECT p.id, u.id,  u.username, u.avatarUrl, p.createdAt, p.content, " +
                "(SELECT COUNT(*) FROM PostLike pl WHERE pl.postId = p.id) AS number_of_likes, " +
                "(SELECT COUNT(*) FROM Post p1 WHERE p1.parentPostId = p.id) AS number_of_comments, " +
                "(SELECT COUNT(*) FROM PostRepost pr WHERE pr.postId = p.id) AS number_of_reposts, " +
                "(CASE WHEN EXISTS (SELECT 1 FROM PostLike pl WHERE pl.postId = p.id AND pl.userId = :currentUserId) THEN TRUE ELSE FALSE END) AS is_liked, " +
                "(SELECT string_agg(pi.imageUrl, ',') FROM PostImage pi WHERE pi.postId = p.id) AS post_image_urls " +
                "FROM Post p " +
                "INNER JOIN User u ON p.userId = u.id " +
                "WHERE TRUE ";

        if (type == ConditionType.USER) {
            jpql += "AND p.userId = :userId ";
        } else if (type == ConditionType.ROOM) {
            jpql += "AND p.roomId IN :roomIds ";
        }

        if (status != null) {
            jpql += "AND p.status = :status ";
        }

        if (after != null) {
            jpql += "AND p.createdAt < :after ";
        }

        return jpql + "ORDER BY p.createdAt DESC, p.id DESC ";
    }

    @NonNull
    private Slice<PostOverviewDTO> getSlicePostOverviewDTOS(Pageable pageable, TypedQuery<Object[]> query) {

        List<Object[]> resultList = query.getResultList();
        boolean hasNext = resultList.size() > pageable.getPageSize();

        if (hasNext) {
            resultList.removeLast();
        }

        List<PostOverviewDTO> postList = resultList.stream()
                .map(row -> PostOverviewDTO.builder()
                        .id((String) row[0])
                        .idOfCreator((String) row[1])
                        .usernameOfCreator((String) row[2])
                        .avatarUrlOfCreator((String) row[3])
                        .createdAt((Instant) row[4])
                        .content((String) row[5])
                        .numberOfLikes(((Long) row[6]).intValue())
                        .numberOfComments(((Long) row[7]).intValue())
                        .numberOfReposts(((Long) row[8]).intValue())
                        .isLiked((boolean) row[9])
                        .postImageUrls(row[10] == null ? null :
                                List.of(((String) row[10]).split(","))
                        )
                        .build()
                )
                .toList();

        return new SliceImpl<>(postList, pageable, hasNext);
    }

    enum ConditionType {
        USER, ROOM
    }
}
