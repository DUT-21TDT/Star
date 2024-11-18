package com.pbl.star.repositories.extensions.impl;

import com.pbl.star.dtos.query.post.PendingPostForUserDTO;
import com.pbl.star.dtos.query.post.PostForModDTO;
import com.pbl.star.dtos.query.post.PostForUserDTO;
import com.pbl.star.enums.PostStatus;
import com.pbl.star.repositories.extensions.PostRepositoryExtension;
import com.pbl.star.utils.AuthUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class PostRepositoryExtensionImpl implements PostRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Slice<PostForUserDTO> findExistPostsOfUserByStatus(Pageable pageable, Instant after, PostStatus status, String userId) {

        String currentUserId = AuthUtil.getCurrentUser().getId();

        String sql = getPostForUserQuery(ConditionType.USER, after, status);

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query
                .setParameter("userId", userId)
                .setParameter("currentUserId", currentUserId)
                .setParameter("status", status.name())
                .setFirstResult(0)
                .setMaxResults(pageable.getPageSize() + 1);

        if (after != null) {
            query.setParameter("after", after);
        }

        return getSlicePostForUserDTOS(pageable, query);
    }

    @Override
    public Slice<PendingPostForUserDTO> findExistPendingPostsOfUser(Pageable pageable, Instant after, String userId) {

        String sql = "SELECT p.post_id, u.user_id, u.username, u.avatar_url, p.created_at, p.content, p.status, " +
                "(SELECT string_agg(pi.image_url, ',' ORDER BY pi.position) FROM post_image pi WHERE pi.post_id = p.post_id) AS post_image_urls, " +
                "p.room_id, r.name " +
                "FROM post p " +
                "INNER JOIN \"user\" u ON p.user_id = u.user_id " +
                "INNER JOIN room r ON p.room_id = r.room_id " +
                "WHERE p.user_id = :userId " +
                "AND p.is_deleted = false " +
                "AND p.status = 'PENDING' " +
                "AND p.parent_post_id is null " +
                (after == null ? "" : "AND p.created_at < :after ") +
                "ORDER BY p.created_at DESC, p.post_id DESC ";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query
                .setParameter("userId", userId)
                .setFirstResult(0)
                .setMaxResults(pageable.getPageSize() + 1);

        if (after != null) {
            query.setParameter("after", after);
        }

        return getSlicePendingPostForUserDTOS(pageable, query);
    }

    private Slice<PendingPostForUserDTO> getSlicePendingPostForUserDTOS(Pageable pageable, Query query) {
        List<Object[]> resultList = query.getResultList();
        boolean hasNext = resultList.size() > pageable.getPageSize();

        if (hasNext) {
            resultList.removeLast();
        }

        List<PendingPostForUserDTO> postList = resultList.stream()
                .map(row -> (PendingPostForUserDTO) PendingPostForUserDTO.builder()
                        .id((String) row[0])
                        .idOfCreator((String) row[1])
                        .usernameOfCreator((String) row[2])
                        .avatarUrlOfCreator((String) row[3])
                        .createdAt((Instant) row[4])
                        .content((String) row[5])
                        .status(PostStatus.valueOf((String) row[6]))
                        .postImageUrls(row[7] == null ? null :
                                List.of(((String) row[7]).split(","))
                        )
                        .idOfRoom((String) row[8])
                        .nameOfRoom((String) row[9])
                        .build()
                )
                .toList();

        return new SliceImpl<>(postList, pageable, hasNext);
    }

    @Override
    public Slice<PostForUserDTO> findExistPostsInRoomsByStatusAsUser(Pageable pageable, Instant after, PostStatus
            status, String... roomIds) {
        String currentUserId = AuthUtil.getCurrentUser().getId();

        String sql = getPostForUserQuery(ConditionType.ROOM, after, status);

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query
                .setParameter("roomIds", List.of(roomIds))
                .setParameter("currentUserId", currentUserId)
                .setParameter("status", status.name())
                .setFirstResult(0)
                .setMaxResults(pageable.getPageSize() + 1);

        if (after != null) {
            query.setParameter("after", after);
        }

        return getSlicePostForUserDTOS(pageable, query);
    }

    private static String getPostForUserQuery(ConditionType type, Instant after, PostStatus status) {

        String sql = "SELECT p.post_id, u.user_id, u.username, u.avatar_url, p.created_at, p.content, " +
                "   (SELECT COUNT(*) FROM post_like pl WHERE pl.post_id = p.post_id) AS number_of_likes, " +
                "   (SELECT COUNT(*) FROM post p1 WHERE p1.parent_post_id = p.post_id and p1.is_deleted = FALSE) AS number_of_comments, " +
                "   (SELECT COUNT(*) FROM post_repost pr WHERE pr.post_id = p.post_id) AS number_of_reposts, " +
                "   (CASE WHEN EXISTS (SELECT 1 FROM post_like pl WHERE pl.post_id = p.post_id AND pl.user_id = :currentUserId) THEN TRUE ELSE FALSE END) AS is_liked, " +
                "(SELECT string_agg(pi.image_url, ',' ORDER BY pi.position) FROM post_image pi WHERE pi.post_id = p.post_id) AS post_image_urls, " +
                "p.room_id, r.name " +
                "FROM post p " +
                "INNER JOIN \"user\" u ON p.user_id = u.user_id " +
                "INNER JOIN room r ON p.room_id = r.room_id " +
                "WHERE p.is_deleted = FALSE " +
                "AND p.parent_post_id is null ";


        if (type == ConditionType.USER) {
            sql += "AND p.user_id = :userId ";
        } else if (type == ConditionType.ROOM) {
            sql += "AND p.room_id IN :roomIds ";
        }

        if (status != null) {
            sql += "AND p.status = :status ";
        }

        if (after != null) {
            sql += "AND p.created_at < :after ";
        }

        return sql + "ORDER BY p.created_at DESC, p.post_id DESC ";
    }

    @NonNull
    private Slice<PostForUserDTO> getSlicePostForUserDTOS(Pageable pageable, Query query) {

        List<Object[]> resultList = query.getResultList();
        boolean hasNext = resultList.size() > pageable.getPageSize();

        if (hasNext) {
            resultList.removeLast();
        }

        List<PostForUserDTO> postList = resultList.stream()
                .map(this::toPostForUserDTO)
                .toList();

        return new SliceImpl<>(postList, pageable, hasNext);
    }

    @Override
    public Slice<PostForModDTO> findExistPostsInRoomByStatusAsMod(Pageable pageable, Instant after, PostStatus status, String roomId) {

        String sql = getPostForModQuery(after, status);

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query
                .setParameter("roomId", roomId)
                .setParameter("status", status.name())
                .setFirstResult(0)
                .setMaxResults(pageable.getPageSize() + 1);

        if (after != null) {
            query.setParameter("after", after);
        }

        return getSlicePostForModDTOS(pageable, query);
    }

    private static String getPostForModQuery(Instant after, PostStatus status) {

        String sql = "SELECT p.post_id, u.user_id, u.username, u.avatar_url, p.created_at, p.content, p.status, p.violence_score, " +
                "(SELECT string_agg(pi.image_url, ',' ORDER BY pi.position) FROM post_image pi WHERE pi.post_id = p.post_id) AS post_image_urls, " +
                "p.room_id, p.moderated_by, u1.username, p.moderated_at " +
                "FROM post p " +
                "INNER JOIN \"user\" u ON p.user_id = u.user_id " +
                "LEFT JOIN \"user\" u1 ON p.moderated_by = u1.user_id " +
                "WHERE p.room_id = :roomId " +
                "AND p.is_deleted = false " +
                "AND p.parent_post_id is null ";

        if (status != null) {
            sql += "AND p.status = :status ";
        }

        if (after != null) {
            sql += "AND p.created_at < :after ";
        }

        return sql + "ORDER BY p.created_at DESC, p.post_id DESC ";
    }

    @NonNull
    private Slice<PostForModDTO> getSlicePostForModDTOS(Pageable pageable, Query query) {

        List<Object[]> resultList = query.getResultList();
        boolean hasNext = resultList.size() > pageable.getPageSize();

        if (hasNext) {
            resultList.removeLast();
        }

        List<PostForModDTO> postList = resultList.stream()
                .map(row -> (PostForModDTO) PostForModDTO.builder()
                        .id((String) row[0])
                        .idOfCreator((String) row[1])
                        .usernameOfCreator((String) row[2])
                        .avatarUrlOfCreator((String) row[3])
                        .createdAt((Instant) row[4])
                        .content((String) row[5])
                        .status(PostStatus.valueOf((String) row[6]))
                        .violenceScore((Integer) row[7])
                        .postImageUrls(row[8] == null ? null :
                                List.of(((String) row[8]).split(","))
                        )
                        .idOfRoom((String) row[9])
                        .idOfModerator((String) row[10])
                        .usernameOfModerator((String) row[11])
                        .moderatedAt((Instant) row[12])
                        .build()
                )
                .toList();

        return new SliceImpl<>(postList, pageable, hasNext);
    }


    @Override
    public Optional<PostForUserDTO> findExistPostByIdAsUser(String currentUserId, String postId) {

        String sql = "SELECT p.post_id, u.user_id, u.username, u.avatar_url, p.created_at, p.content, " +
                "   (SELECT COUNT(*) FROM post_like pl WHERE pl.post_id = p.post_id) AS number_of_likes, " +
                "   (SELECT COUNT(*) FROM post p1 WHERE p1.parent_post_id = p.post_id and p1.is_deleted = FALSE) AS number_of_comments, " +
                "   (SELECT COUNT(*) FROM post_repost pr WHERE pr.post_id = p.post_id) AS number_of_reposts, " +
                "   (CASE WHEN EXISTS (SELECT 1 FROM post_like pl WHERE pl.post_id = p.post_id AND pl.user_id = :currentUserId) THEN TRUE ELSE FALSE END) AS is_liked, " +
                "(SELECT string_agg(pi.image_url, ',' ORDER BY pi.position) FROM post_image pi WHERE pi.post_id = p.post_id) AS post_image_urls, " +
                "p.room_id, r.name, p.parent_post_id " +
                "FROM post p " +
                "INNER JOIN \"user\" u ON p.user_id = u.user_id " +
                "INNER JOIN room r ON p.room_id = r.room_id " +
                "WHERE p.is_deleted = false " +
                "AND p.post_id = :postId ";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query
                .setParameter("currentUserId", currentUserId)
                .setParameter("postId", postId);

        Object[] result = (Object[]) query.getResultStream().findFirst().orElse(null);

        return result == null ? Optional.empty() : Optional.of(
                PostForUserDTO.builder()
                        .id((String) result[0])
                        .idOfCreator((String) result[1])
                        .usernameOfCreator((String) result[2])
                        .avatarUrlOfCreator((String) result[3])
                        .createdAt((Instant) result[4])
                        .content((String) result[5])
                        .numberOfLikes(((Long) result[6]).intValue())
                        .numberOfComments(((Long) result[7]).intValue())
                        .numberOfReposts(((Long) result[8]).intValue())
                        .isLiked((boolean) result[9])
                        .postImageUrls(result[10] == null ? null : List.of(((String) result[10]).split(",")))
                        .idOfRoom((String) result[11])
                        .nameOfRoom((String) result[12])
                        .idOfParentPost((String) result[13])
                        .build()
        );
    }

    private PostForUserDTO toPostForUserDTO(Object[] source) {
        return PostForUserDTO.builder()
                .id((String) source[0])
                .idOfCreator((String) source[1])
                .usernameOfCreator((String) source[2])
                .avatarUrlOfCreator((String) source[3])
                .createdAt((Instant) source[4])
                .content((String) source[5])
                .numberOfLikes(((Long) source[6]).intValue())
                .numberOfComments(((Long) source[7]).intValue())
                .numberOfReposts(((Long) source[8]).intValue())
                .isLiked((boolean) source[9])
                .postImageUrls(source[10] == null ? null : List.of(((String) source[10]).split(",")))
                .idOfRoom((String) source[11])
                .nameOfRoom((String) source[12])
                .build();
    }

    @Override
    public Slice<PostForUserDTO> findExistRepliesOfPostAsUser(Pageable pageable, Instant after, String currentUserId, String postId) {

        String sql = "SELECT p.post_id, u.user_id, u.username, u.avatar_url, p.created_at, p.content, " +
                "   (SELECT COUNT(*) FROM post_like pl WHERE pl.post_id = p.post_id) AS number_of_likes, " +
                "   (SELECT COUNT(*) FROM post p1 WHERE p1.parent_post_id = p.post_id and p1.is_deleted = FALSE) AS number_of_comments, " +
                "   (SELECT COUNT(*) FROM post_repost pr WHERE pr.post_id = p.post_id) AS number_of_reposts, " +
                "   (CASE WHEN EXISTS (SELECT 1 FROM post_like pl WHERE pl.post_id = p.post_id AND pl.user_id = :currentUserId) THEN TRUE ELSE FALSE END) AS is_liked, " +
                "(SELECT string_agg(pi.image_url, ',' ORDER BY pi.position) FROM post_image pi WHERE pi.post_id = p.post_id) AS post_image_urls," +
                "p.parent_post_id " +
                "FROM post p " +
                "INNER JOIN \"user\" u ON p.user_id = u.user_id " +
                "WHERE p.is_deleted = false " +
                "AND p.parent_post_id = :postId " +
                "AND p.status = 'APPROVED' " +
                (after == null ? "" : "AND p.created_at < :after ") +
                "ORDER BY p.created_at DESC, p.post_id DESC ";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query
                .setParameter("currentUserId", currentUserId)
                .setParameter("postId", postId)
                .setMaxResults(pageable.getPageSize() + 1);

        if (after != null) {
            query.setParameter("after", after);
        }

        List<Object[]> resultList = query.getResultList();

        boolean hasNext = resultList.size() > pageable.getPageSize();

        if (hasNext) {
            resultList.removeLast();
        }

        List<PostForUserDTO> postList = resultList.stream()
                .map(row -> (PostForUserDTO) PostForUserDTO.builder()
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
                        .idOfParentPost((String) row[11])
                        .build()

                )
                .toList();

        return new SliceImpl<>(postList, pageable, hasNext);
    }


    enum ConditionType {
        USER, ROOM
    }
}
