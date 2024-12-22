package com.pbl.star.repositories.extensions.impl;

import com.pbl.star.models.projections.post.PostForUser;
import com.pbl.star.models.projections.post.RepostOnWall;
import com.pbl.star.repositories.extensions.PostRepostRepositoryExtension;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.time.Instant;
import java.util.List;

public class PostRepostRepositoryExtensionImpl implements PostRepostRepositoryExtension {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RepostOnWall> findRepostsOnWallAsUser(int limit, Instant after, String currentUserId, String targetUserId) {
        String sql = "SELECT p.post_id, u.user_id, u.username, u.avatar_url, p.created_at, p.content, " +
                "   (SELECT COUNT(*) FROM post_like pl WHERE pl.post_id = p.post_id) AS number_of_likes, " +
                "   (SELECT COUNT(*) FROM post p1 WHERE p1.parent_post_id = p.post_id and p1.is_deleted = FALSE) AS number_of_comments, " +
                "   (SELECT COUNT(*) FROM post_repost pr WHERE pr.post_id = p.post_id) AS number_of_reposts, " +
                "   (CASE WHEN EXISTS (SELECT 1 FROM post_like pl WHERE pl.post_id = p.post_id AND pl.user_id = :currentUserId) THEN TRUE ELSE FALSE END) AS is_liked, " +
                "   (CASE WHEN EXISTS (SELECT 1 FROM post_repost pr WHERE pr.post_id = p.post_id AND pr.user_id = :currentUserId) THEN TRUE ELSE FALSE END) AS is_reposted, " +
                "(SELECT string_agg(pi.image_url, ',' ORDER BY pi.position) FROM post_image pi WHERE pi.post_id = p.post_id) AS post_image_urls, " +
                "p.room_id, r.name, pr.repost_at, o.username, pr.caption " +
                "FROM post_repost pr " +
                "LEFT JOIN post p " +
                "ON pr.post_id = p.post_id " +
                "INNER JOIN \"user\" o " +
                "ON pr.user_id = o.user_id " +
                "INNER JOIN \"user\" u " +
                "ON p.user_id = u.user_id " +
                "INNER JOIN room r " +
                "ON p.room_id = r.room_id " +
                "WHERE pr.user_id = :targetUserId " +
                "AND p.is_deleted = FALSE " +
                "AND p.is_hidden = FALSE " +
                (after != null ? "AND pr.repost_at < :after " : "") +
                "ORDER BY pr.repost_at DESC, pr.post_repost_id";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("currentUserId", currentUserId)
                .setParameter("targetUserId", targetUserId)
                .setMaxResults(limit);

        if (after != null) {
            query.setParameter("after", after);
        }

        List<Object[]> resultList = query.getResultList();


        return resultList.stream().map(
                row -> {
                    PostForUser post = PostForUser.builder()
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
                            .isReposted((boolean) row[10])
                            .postImageUrls(row[11] == null ? null : List.of(((String) row[11]).split(",")))
                            .idOfRoom((String) row[12])
                            .nameOfRoom((String) row[13])
                            .build();

                    return RepostOnWall.builder()
                            .repostedPost(post)
                            .repostedAt((Instant) row[14])
                            .repostedByUsername((String) row[15])
                            .caption((String) row[16])
                            .build();
                }
        ).toList();
    }
}
