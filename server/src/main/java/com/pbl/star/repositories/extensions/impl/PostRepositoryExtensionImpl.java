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

import java.time.Instant;
import java.util.List;

public class PostRepositoryExtensionImpl implements PostRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Slice<PostOverviewDTO> findPostOverviewsByUserAndStatus(String userId, Pageable pageable, Instant after, PostStatus status) {

        String currentUserId = AuthUtil.getCurrentUser().getId();

        String jpql = getPostOverviewQuery(after);

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

        List<Object[]> resultList = query.getResultList();
        boolean hasNext = resultList.size() > pageable.getPageSize();

        if (hasNext) {
            resultList.removeLast();
        }

        List<PostOverviewDTO> postList = resultList.stream()
                .map(row -> PostOverviewDTO.builder()
                        .id((String) row[0])
                        .usernameOfCreator((String) row[1])
                        .avatarUrlOfCreator((String) row[2])
                        .createdAt((Instant) row[3])
                        .content((String) row[4])
                        .numberOfLikes(((Long) row[5]).intValue())
                        .numberOfComments(((Long) row[6]).intValue())
                        .numberOfReposts(((Long) row[7]).intValue())
                        .isLiked((boolean) row[8])
                        .postImageUrls(row[9] == null ? null :
                                List.of(((String) row[9]).split(","))
                        )
                        .build()
                )
                .toList();

        return new SliceImpl<>(postList, pageable, hasNext);
    }

    private static String getPostOverviewQuery(Instant after) {
        String jpql = "SELECT p.id, u.username, u.avatarUrl, p.createdAt, p.content, " +
                "(SELECT COUNT(*) FROM PostLike pl WHERE pl.postId = p.id) AS number_of_likes, " +
                "(SELECT COUNT(*) FROM Post p1 WHERE p1.parentPostId = p.id) AS number_of_comments, " +
                "(SELECT COUNT(*) FROM PostRepost pr WHERE pr.postId = p.id) AS number_of_reposts, " +
                "(CASE WHEN EXISTS (SELECT 1 FROM PostLike pl WHERE pl.postId = p.id AND pl.userId = :currentUserId) THEN TRUE ELSE FALSE END) AS is_liked, " +
                "(SELECT string_agg(pi.imageUrl, ',') FROM PostImage pi WHERE pi.postId = p.id) AS post_image_urls " +
                "FROM Post p " +
                "INNER JOIN User u ON p.userId = u.id " +
                "WHERE p.userId = :userId " +
                "AND p.status = :status ";

        if (after != null) {
            jpql += "AND p.createdAt < :after ";
        }

        return jpql + "ORDER BY p.createdAt DESC, p.id DESC ";
    }
}
