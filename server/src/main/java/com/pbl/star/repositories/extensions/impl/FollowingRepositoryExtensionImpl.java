package com.pbl.star.repositories.extensions.impl;

import com.pbl.star.models.projections.follow.FollowCount;
import com.pbl.star.enums.FollowStatus;
import com.pbl.star.models.projections.user.*;
import com.pbl.star.repositories.extensions.FollowingRepositoryExtension;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class FollowingRepositoryExtensionImpl implements FollowingRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<OnFollowProfile> getFollowingsOfUser(int limit, Instant after, String currentUserId, String targetUserId) {

        String jpql;

        if (currentUserId.equals(targetUserId)) {
            jpql = "SELECT u.id, u.username, u.avatarUrl, u.firstName, u.lastName, 'FOLLOWING' as follow_status, f.followAt " +
                    "FROM Following f " +
                    "INNER JOIN User u " +
                    "ON f.followeeId = u.id " +
                    "WHERE f.followerId = :targetUserId " +
                    "AND f.status = 'ACCEPTED' " +
                    (after != null ? "AND f.followAt < :after " : "") +
                    "ORDER BY f.followAt DESC, f.id";
        } else {
            jpql = "SELECT u.id, u.username, u.avatarUrl, u.firstName, u.lastName, " +
                    "(case " +
                    "   when not exists (select 1 from Following f where f.followerId = :currentId and f.followeeId = u.id) then 'NOT_FOLLOWING' " +
                    "   when exists (select 1 from Following f where f.followerId = :currentId and f.followeeId = u.id and f.status='ACCEPTED') then 'FOLLOWING' " +
                    "   else 'REQUESTED' end) " +
                    "   as follow_status, " +
                    "f.followAt " +
                    "FROM Following f " +
                    "INNER JOIN User u " +
                    "ON f.followeeId = u.id " +
                    "WHERE f.followerId = :targetUserId " +
                    "AND f.status = 'ACCEPTED' " +
                    (after != null ? "AND f.followAt < :after " : "") +
                    "ORDER BY f.followAt DESC, f.id";
        }

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);

        if (!currentUserId.equals(targetUserId)) {
            query.setParameter("currentId", currentUserId);
        }

        query.setParameter("targetUserId", targetUserId)
                .setMaxResults(limit);

        if (after != null) {
            query.setParameter("after", after);
        }

        List<Object[]> resultList = query.getResultList();
        return resultList.stream()
                .map(row -> OnFollowProfile.builder()
                        .userId((String) row[0])
                        .username((String) row[1])
                        .avatarUrl((String) row[2])
                        .firstName((String) row[3])
                        .lastName((String) row[4])
                        .followStatus(FollowStatus.valueOf((String) row[5]))
                        .followAt((Instant) row[6])
                        .build())
                .toList();
    }

    @Override
    public List<OnFollowProfile> getFollowersOfUser(int limit, Instant after, String currentUserId, String targetUserId) {

        String jpql = "SELECT u.id, u.username, u.avatarUrl, u.firstName, u.lastName, " +
                "(case " +
                "   when not exists (select 1 from Following f where f.followerId = :currentId and f.followeeId = u.id) then 'NOT_FOLLOWING' " +
                "   when exists (select 1 from Following f where f.followerId = :currentId and f.followeeId = u.id and f.status='ACCEPTED') then 'FOLLOWING' " +
                "   else 'REQUESTED' end) " +
                "   as follow_status, " +
                "f.followAt " +
                "FROM Following f " +
                "INNER JOIN User u " +
                "ON f.followerId = u.id " +
                "WHERE f.followeeId = :targetUserId " +
                "AND f.status = 'ACCEPTED' " +
                (after != null ? "AND f.followAt < :after " : "") +
                "ORDER BY f.followAt DESC, f.id";

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query.setParameter("currentId", currentUserId)
                .setParameter("targetUserId", targetUserId)
                .setMaxResults(limit);

        if (after != null) {
            query.setParameter("after", after);
        }

        List<Object[]> resultList = query.getResultList();
        return resultList.stream()
                .map(row -> OnFollowProfile.builder()
                        .userId((String) row[0])
                        .username((String) row[1])
                        .avatarUrl((String) row[2])
                        .firstName((String) row[3])
                        .lastName((String) row[4])
                        .followStatus(FollowStatus.valueOf((String) row[5]))
                        .followAt((Instant) row[6])
                        .build())
                .toList();
    }

//    @NonNull
//    private Slice<OnFollowProfile> getOnFollowProfiles(Pageable pageable, Instant after, String currentUserId, String targetUserId, String jpql) {
//        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
//
//        query.setParameter("currentId", currentUserId)
//                .setParameter("targetUserId", targetUserId)
//                .setMaxResults(pageable.getPageSize() + 1);
//
//        if (after != null) {
//            query.setParameter("after", after);
//        }
//
//        return toOnFollowProfileSlice(query, pageable);
//    }

//    private Slice<OnFollowProfile> toOnFollowProfileSlice(TypedQuery<Object[]> query, Pageable pageable) {
//
//        List<Object[]> resultList = query.getResultList();
//        boolean hasNext = resultList.size() > pageable.getPageSize();
//
//        if (hasNext) {
//            resultList.removeLast();
//        }
//
//        List<OnFollowProfile> content = resultList.stream()
//                .map(row -> OnFollowProfile.builder()
//                        .userId((String) row[0])
//                        .username((String) row[1])
//                        .avatarUrl((String) row[2])
//                        .firstName((String) row[3])
//                        .lastName((String) row[4])
//                        .followStatus(FollowStatus.valueOf((String) row[5]))
//                        .followAt((Instant) row[6])
//                        .build())
//                .toList();
//
//        return new SliceImpl<>(content, pageable, hasNext);
//    }

    @Override
    public List<OnFollowRequestProfile> getFollowRequestsOfUser(int limit, Instant after, String userId) {
        String jpql = "SELECT u.id, u.username, u.avatarUrl, u.firstName, u.lastName, f.id, f.followAt " +
                "FROM Following f " +
                "INNER JOIN User u " +
                "ON f.followerId = u.id " +
                "WHERE f.followeeId = :userId " +
                "AND f.status = 'PENDING' " +
                (after != null ? "AND f.followAt < :after " : "") +
                "ORDER BY f.followAt DESC, f.id";

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);

        query.setParameter("userId", userId)
                .setMaxResults(limit);

        if (after != null) {
            query.setParameter("after", after);
        }

        List<Object[]> resultList = query.getResultList();
        return resultList.stream()
                .map(row -> OnFollowRequestProfile.builder()
                        .userId((String) row[0])
                        .username((String) row[1])
                        .avatarUrl((String) row[2])
                        .firstName((String) row[3])
                        .lastName((String) row[4])
                        .followingId((String) row[5])
                        .followAt((Instant) row[6])
                        .build())
                .toList();
    }

//    private Slice<OnFollowRequestProfile> toOnFollowRequestProfileSlice(TypedQuery<Object[]> query, Pageable pageable) {
//
//        List<Object[]> resultList = query.getResultList();
//        boolean hasNext = resultList.size() > pageable.getPageSize();
//
//        if (hasNext) {
//            resultList.removeLast();
//        }
//
//        List<OnFollowRequestProfile> content = resultList.stream()
//                .map(row -> OnFollowRequestProfile.builder()
//                        .userId((String) row[0])
//                        .username((String) row[1])
//                        .avatarUrl((String) row[2])
//                        .firstName((String) row[3])
//                        .lastName((String) row[4])
//                        .followingId((String) row[5])
//                        .followAt((Instant) row[6])
//                        .build())
//                .toList();
//
//        return new SliceImpl<>(content, pageable, hasNext);
//    }

    @Override
    public FollowCount countFollowSection(String currentUserId, String targetUserId) {

        String jpql;
        boolean isCurrentUser = currentUserId.equals(targetUserId);

        if (!isCurrentUser) {
            jpql = "SELECT (" +
                    "(SELECT COUNT(f) FROM Following f WHERE f.followerId = :targetUserId AND f.status = 'ACCEPTED'), " +
                    "(SELECT COUNT(f) FROM Following f WHERE f.followeeId = :targetUserId AND f.status = 'ACCEPTED'), " +
                    "0)";
        } else {
            jpql = "SELECT " +
                    "(SELECT COUNT(f) FROM Following f WHERE f.followerId = :targetUserId AND f.status = 'ACCEPTED'), " +
                    "(SELECT COUNT(f) FROM Following f WHERE f.followeeId = :targetUserId AND f.status = 'ACCEPTED'), " +
                    "(SELECT COUNT(f) FROM Following f WHERE f.followeeId = :targetUserId AND f.status = 'PENDING')";
        }

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query.setParameter("targetUserId", targetUserId);

        Object[] result = query.getSingleResult();

        return FollowCount.builder()
                .followingsCount(((Long) result[0]).intValue())
                .followersCount(((Long) result[1]).intValue())
                .followRequestsCount(
                        isCurrentUser ? ((Long) result[2]).intValue() : null
                )
                .build();
    }

    @Override
    public List<OnSuggestionProfile> suggestFollow(String currentUserId, int limit, List<Integer> weights) {
        String sql = "WITH indirect_follow AS ( " +
                "    -- Điểm dựa trên follow gián tiếp (x điểm)\n " +
                "    SELECT  " +
                "        f1.followee_id AS target_user_id,  " +
                "        COUNT(*) * :x AS score " +
                "    FROM  " +
                "        following f1 " +
                "WHERE  " +
                "f1.follower_id = ANY ( " +
                "    ARRAY( " +
                "        SELECT f2.followee_id " +
                "        FROM following f2 " +
                "        WHERE f2.follower_id =  :currentUserId " +
                "AND f2.status = 'ACCEPTED' " +
                "    ) " +
                ")  " +
                "AND f1.status = 'ACCEPTED' " +
                "        AND f1.followee_id !=  :currentUserId -- Loại trừ chính người dùng\n " +
                "    GROUP BY  " +
                "        f1.followee_id " +
                "), " +
                "mutual_follow AS ( " +
                "    -- Điểm dựa trên follow chung (y điểm)\n " +
                "    SELECT  " +
                "        f1.follower_id AS target_user_id,  " +
                "        COUNT(*) * :y AS score " +
                "    FROM  " +
                "        following f1 " +
                "    WHERE " +
                "f1.followee_id = ANY ( " +
                "ARRAY( " +
                "SELECT f2.followee_id " +
                "        FROM following f2 " +
                "        WHERE f2.follower_id =  :currentUserId " +
                "AND f2.status = 'ACCEPTED' " +
                ") " +
                ") " +
                "AND f1.status = 'ACCEPTED' " +
                "        AND f1.follower_id !=  :currentUserId -- Loại trừ chính người dùng\n " +
                "    GROUP BY  " +
                "        f1.follower_id " +
                "), " +
                "common_room AS ( " +
                "    -- Điểm dựa trên room chung (z điểm)\n " +
                "    SELECT  " +
                "        ur2.user_id AS target_user_id,  " +
                "        COUNT(*) * :z AS score " +
                "    FROM  " +
                "        user_room ur1 " +
                "    JOIN  " +
                "        user_room ur2  " +
                "    ON  " +
                "        ur1.room_id = ur2.room_id " +
                "    WHERE  " +
                "        ur1.user_id =  :currentUserId  " +
                "        AND ur2.user_id !=  :currentUserId -- Loại trừ chính người dùng\n " +
                "    GROUP BY  " +
                "        ur2.user_id " +
                "), " +
                "total_scores AS ( " +
                "    -- Tổng hợp điểm từ tất cả các nguồn\n " +
                "    SELECT  " +
                "        target_user_id, " +
                "        COALESCE(SUM(CASE WHEN source = 'indirect' THEN score ELSE 0 END), 0) AS indirect_score, " +
                "        COALESCE(SUM(CASE WHEN source = 'mutual' THEN score ELSE 0 END), 0) AS mutual_score, " +
                "        COALESCE(SUM(CASE WHEN source = 'common' THEN score ELSE 0 END), 0) AS common_score, " +
                "        COALESCE(SUM(score), 0) AS total_score " +
                "    FROM ( " +
                "        SELECT target_user_id, score, 'indirect' AS source FROM indirect_follow " +
                "        UNION ALL " +
                "        SELECT target_user_id, score, 'mutual' AS source FROM mutual_follow " +
                "        UNION ALL " +
                "        SELECT target_user_id, score, 'common' AS source FROM common_room " +
                "    ) all_scores " +
                "    GROUP BY target_user_id " +
                "), " +
                "filtered_scores AS ( " +
                "    -- Loại bỏ những người dùng đã follow\n " +
                "    SELECT  " +
                "        ts.target_user_id, " +
                "        ts.total_score, " +
                "        ts.indirect_score, " +
                "        ts.mutual_score, " +
                "        ts.common_score " +
                "    FROM  " +
                "        total_scores ts " +
                "    LEFT JOIN  " +
                "        following f  " +
                "    ON  " +
                "        f.follower_id =  :currentUserId " +
                "        AND ts.target_user_id = f.followee_id " +
                "    WHERE  " +
                "        f.followee_id IS NULL -- Chỉ lấy những người chưa follow\n " +
                ") " +
                "-- Lấy top 20 người có điểm cao nhất\n " +
                "SELECT  " +
                "    u.user_id,  " +
                "    u.username,  " +
                "    u.avatar_url,  " +
                "    u.first_name,  " +
                "    u.last_name,  " +
                "    fs.indirect_score AS iscore, " +
                "    fs.mutual_score AS mscore, " +
                "    fs.common_score AS cscore, " +
                "    CASE WHEN ( " +
                " u.register_at >= NOW() - INTERVAL '1 MONTH' " +
                ") THEN fs.total_score + 2 " +
                "ELSE fs.total_score " +
                "END as total_score " +
                "FROM  " +
                "    filtered_scores fs " +
                "JOIN  " +
                "    \"user\" u  " +
                "ON  " +
                "    fs.target_user_id = u.user_id " +
                "ORDER BY  " +
                "    fs.total_score DESC ";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("currentUserId", currentUserId)
                .setParameter("x", weights.get(0))
                .setParameter("y", weights.get(1))
                .setParameter("z", weights.get(2))
                .setMaxResults(limit);

        List<Object[]> resultList = query.getResultList();

        return resultList.stream()
                .map(row -> OnSuggestionProfile.builder()
                        .userId((String) row[0])
                        .username((String) row[1])
                        .avatarUrl((String) row[2])
                        .firstName((String) row[3])
                        .lastName((String) row[4])
                        .mutualFriendRelation(MutualRelation.builder()
                                .score(((BigDecimal) row[5]).intValueExact())
                                .count(((BigDecimal) row[5]).intValueExact() / weights.get(0))
                                .build()
                        )
                        .mutualFollowingRelation(MutualRelation.builder()
                                .score(((BigDecimal) row[6]).intValueExact())
                                .count(((BigDecimal) row[6]).intValueExact() / weights.get(1))
                                .build()
                        )
                        .commonRoomRelation(MutualRelation.builder()
                                .score(((BigDecimal) row[7]).intValueExact())
                                .count(((BigDecimal) row[7]).intValueExact() / weights.get(2))
                                .build()
                        )
                        .totalRelationScore(((BigDecimal) row[8]).intValueExact())
                        .build())
                .toList();
    }

    @Override
    public List<SuggestionRep> findRepresentationMutualFollowing(String currentUserId, List<String> suggestionIds) {

        String sql = "SELECT distinct on (f.follower_id) f.follower_id, f.followee_id,  u.username " +
                "FROM following f " +
                "INNER JOIN \"user\" u " +
                "ON f.followee_id = u.user_id " +
                "WHERE f.followee_id = ANY ( " +
                "    ARRAY( " +
                "        SELECT f1.followee_id " +
                "        FROM following f1 " +
                "        WHERE f1.follower_id =  :currentUserId " +
                "AND f1.status = 'ACCEPTED' " +
                "    ) " +
                ")  " +
                "AND f.follower_id in :suggestionIds " +
                "AND f.status = 'ACCEPTED'";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("currentUserId", currentUserId)
                .setParameter("suggestionIds", suggestionIds);

        List<Object[]> resultList = query.getResultList();

        return resultList.stream()
                .map(row -> SuggestionRep.builder()
                        .id((String) row[0])
                        .repId((String) row[1])
                        .repName((String) row[2])
                        .build())
                .toList();
    }

    @Override
    public List<SuggestionRep> findRepresentationMutualFriend(String currentUserId, List<String> suggestionIds) {
        String sql = "SELECT DISTINCT ON (f.followee_id) f.followee_id, f.follower_id, u.username " +
                "FROM following f " +
                "INNER JOIN \"user\" u " +
                "ON f.follower_id = u.user_id " +
                "WHERE f.follower_id = ANY ( " +
                "    ARRAY( " +
                "        SELECT f1.followee_id " +
                "        FROM following f1 " +
                "        WHERE f1.follower_id =  :currentUserId " +
                "AND f1.status = 'ACCEPTED' " +
                "    ) " +
                ")  " +
                "AND f.followee_id in :suggestionIds " +
                "AND f.status = 'ACCEPTED'";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("currentUserId", currentUserId)
                .setParameter("suggestionIds", suggestionIds);

        List<Object[]> resultList = query.getResultList();

        return resultList.stream()
                .map(row -> SuggestionRep.builder()
                        .id((String) row[0])
                        .repId((String) row[1])
                        .repName((String) row[2])
                        .build())
                .toList();
    }

    @Override
    public List<SuggestionRep> findRepresentationCommonRoom(String currentUserId, List<String> suggestionIds) {
        String sql = "SELECT distinct on (user_id) user_id, r.room_id, r.name " +
                "FROM user_room ur " +
                "INNER JOIN room r " +
                "ON ur.room_id = r.room_id " +
                "WHERE ur.room_id = ANY( " +
                "ARRAY(SELECT room_id " +
                "FROM user_room ur1 " +
                "WHERE ur1.user_id = :currentUserId " +
                ") " +
                ") " +
                "AND user_id in :suggestionIds ";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("currentUserId", currentUserId)
                .setParameter("suggestionIds", suggestionIds);

        List<Object[]> resultList = query.getResultList();

        return resultList.stream()
                .map(row -> SuggestionRep.builder()
                        .id((String) row[0])
                        .repId((String) row[1])
                        .repName((String) row[2])
                        .build())
                .toList();
    }
}
