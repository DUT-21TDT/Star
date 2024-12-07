package com.pbl.star.repositories.extensions.impl;

import com.pbl.star.dtos.query.user.*;
import com.pbl.star.entities.User;
import com.pbl.star.enums.AccountStatus;
import com.pbl.star.enums.FollowStatus;
import com.pbl.star.enums.Gender;
import com.pbl.star.enums.SortDirection;
import com.pbl.star.repositories.extensions.UserRepositoryExtension;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryExtensionImpl implements UserRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<OnSearchProfile> searchUsers(int limit, String afterId, String currentUserId, String keyword) {
        String sql = getSearchUsersQuery(afterId);

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("keyword", keyword)
                .setParameter("keyword1", "%" + keyword + "%")
                .setParameter("currentId", currentUserId)
                .setFirstResult(0)
                .setMaxResults(limit);

        if (afterId != null) {
            query.setParameter("afterId", afterId);
        }

        List<Object[]> resultList = query.getResultList();
        return resultList.stream()
                .map(row -> OnSearchProfile.builder()
                        .userId((String) row[0])
                        .username((String) row[1])
                        .avatarUrl((String) row[2])
                        .firstName((String) row[3])
                        .lastName((String) row[4])
                        .numberOfFollowers(((Long) row[5]).intValue())
                        .followStatus(FollowStatus.valueOf((String) row[6]))
                        .build())
                .toList();

    }

    private String getSearchUsersQuery(String afterId) {
        String sql = "select u.user_id, u.username, u.avatar_url, u.first_name, u.last_name, " +
                "(select count(*) from following f where f.followee_id = u.user_id and f.status='ACCEPTED') " +
                "   as follower_count, " +
                "(case " +
                "   when not exists (select 1 from Following f where f.follower_id = :currentId and f.followee_id = u.user_id) then 'NOT_FOLLOWING' " +
                "   when exists (select 1 from Following f where f.follower_id = :currentId and f.followee_id = u.user_id and f.status='ACCEPTED') then 'FOLLOWING' " +
                "   else 'REQUESTED' end) " +
                "   as follow_status," +
                "GREATEST(" +
                "    similarity(username, :keyword), " +
                "    similarity(first_name, :keyword), " +
                "    similarity(last_name, :keyword) " +
                ") as similarity " +
                "from \"user\" u " +
                "where role = 'USER' AND status = 'ACTIVE' AND " +
                "(username ilike :keyword1 " +
                "or first_name ilike :keyword1 " +
                "or last_name ilike :keyword1 " +
                "or username % :keyword " +
                "or first_name % :keyword " +
                "or last_name % :keyword) " +
                "order by similarity desc, user_id asc ";

        if (afterId != null) {
            sql += "and user_id > :afterId ";
        }

        return sql;
    }

//    private Slice<OnSearchProfile> getSliceOnSearchProfile(Pageable pageable, Query query) {
//        List<Object[]> resultList = query.getResultList();
//
//        boolean hasNext = resultList.size() > pageable.getPageSize();
//
//        if (hasNext) {
//            resultList.removeLast();
//        }
//
//        List<OnSearchProfile> content = resultList.stream()
//                .map(row -> OnSearchProfile.builder()
//                        .userId((String) row[0])
//                        .username((String) row[1])
//                        .avatarUrl((String) row[2])
//                        .firstName((String) row[3])
//                        .lastName((String) row[4])
//                        .numberOfFollowers(((Long) row[5]).intValue())
//                        .followStatus(FollowStatus.valueOf((String) row[6]))
//                        .build())
//                .toList();
//
//        return new SliceImpl<>(content, pageable, hasNext);
//    }

    @Override
    public OnWallProfile getPublicProfile(String currentId, String targetUserId) {

        String jpql;
        TypedQuery<Object[]> query;
        boolean isCurrentUser = currentId.equals(targetUserId);

        if (isCurrentUser) {
            jpql = "SELECT u.username, u.firstName, u.lastName, u.bio, u.avatarUrl, u.privateProfile, " +
                    "(select count(*) from " +
                    "   Following f " +
                    "   where f.followeeId = u.id and f.status='ACCEPTED') " +
                    "   as follower_count " +
                    "FROM User u " +
                    "WHERE u.id = :userId and u.status = 'ACTIVE'";

            query = entityManager.createQuery(jpql, Object[].class);
            query.setParameter("userId", targetUserId);
        } else {
            jpql = "SELECT u.username, u.firstName, u.lastName, u.bio, u.avatarUrl, u.privateProfile, " +
                    "(select count(*) from " +
                    "   Following f " +
                    "   where f.followeeId = u.id and f.status='ACCEPTED') " +
                    "   as follower_count, " +
                    "(case " +
                    "   when not exists (select 1 from Following f where f.followerId = :currentId and f.followeeId = u.id) then 'NOT_FOLLOWING' " +
                    "   when exists (select 1 from Following f where f.followerId = :currentId and f.followeeId = u.id and f.status='ACCEPTED') then 'FOLLOWING' " +
                    "   else 'REQUESTED' end) " +
                    "   as follow_status " +
                    "FROM User u " +
                    "WHERE u.id = :userId and u.status = 'ACTIVE'";

            query = entityManager.createQuery(jpql, Object[].class);
            query.setParameter("userId", targetUserId);
            query.setParameter("currentId", currentId);
        }

        try {
            Object[] result = query.getSingleResult();
            OnWallProfileUser onWallProfile = OnWallProfileUser.builder()
                    .username((String) result[0])
                    .firstName((String) result[1])
                    .lastName((String) result[2])
                    .bio((String) result[3])
                    .avatarUrl((String) result[4])
                    .privateProfile(result[5] != null && (boolean) result[5])
                    .numberOfFollowers(((Long) result[6]).intValue())
                    .build();

            return OnWallProfile.builder()
                    .publicProfile(onWallProfile)
                    .isCurrentUser(targetUserId.equals(currentId))
                    .followStatus(isCurrentUser ?
                            FollowStatus.NOT_FOLLOWING : FollowStatus.valueOf((String) result[7])
                    )
                    .build();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public PersonalInformation getPersonalInformation(String userId) {
        String jpql = "SELECT u.email, u.username, u.firstName, u.lastName, u.bio, u.avatarUrl, u.dateOfBirth, u.gender, u.registerAt, u.privateProfile " +
                "FROM User u " +
                "WHERE u.id = :userId and u.status = 'ACTIVE'";

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query.setParameter("userId", userId);

        try {
            Object[] result = query.getSingleResult();
            return PersonalInformation.builder()
                    .email((String) result[0])
                    .username((String) result[1])
                    .firstName((String) result[2])
                    .lastName((String) result[3])
                    .bio((String) result[4])
                    .avatarUrl((String) result[5])
                    .dateOfBirth((LocalDate) result[6])
                    .gender(result[7] == null ? null : (Gender) result[7])
                    .registerAt((Instant) result[8])
                    .privateProfile(result[9] != null && (boolean) result[9])
                    .build();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<UserInRoom> findUsersInRoom(String roomId, String keyword) {
        String sql = "SELECT u.user_id, u.username, u.avatar_url, u.first_name, u.last_name, " +
                "GREATEST(" +
                "    similarity(username, :keyword), " +
                "    similarity(first_name, :keyword), " +
                "    similarity(last_name, :keyword) " +
                ") as similarity " +
                "FROM user_room ur " +
                "JOIN \"user\" u " +
                "ON ur.user_id = u.user_id " +
                "WHERE ur.room_id=:roomId AND " +
                "u.role = 'USER' AND u.status = 'ACTIVE' AND " +
                "(u.username ilike :keyword1 " +
                "OR u.username % :keyword) " +
                "ORDER BY similarity DESC, user_id ";

        Query query = entityManager.createNativeQuery(sql, Object[].class);

        query.setParameter("roomId", roomId)
                .setParameter("keyword", keyword)
                .setParameter("keyword1", "%" + keyword + "%")
                .setFirstResult(0)
                .setMaxResults(10);

        List<Object[]> resultList = query.getResultList();

        if (resultList.isEmpty()) {
            return List.of();
        }

        return resultList.stream()
                .map(row -> UserInRoom.builder()
                        .userId((String) row[0])
                        .username((String) row[1])
                        .avatarUrl((String) row[2])
                        .firstName((String) row[3])
                        .lastName((String) row[4])
                        .build())
                .toList();
    }

    @Override
    public Page<OnDashboardProfileDTO> findUsersAsAdmin(Pageable pageable,
                                                        String keyword,
                                                        AccountStatus status,
                                                        String sortBy,
                                                        SortDirection direction) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OnDashboardProfileDTO> criteriaQuery = criteriaBuilder.createQuery(OnDashboardProfileDTO.class);
        Root<User> root = criteriaQuery.from(User.class);

        // List to store predicates (conditions)
        List<Predicate> predicates = new ArrayList<>();

        // Keyword search across multiple attributes
        if (!StringUtils.isEmpty(keyword)) {
            Predicate usernamePredicate = criteriaBuilder.like(root.get("username"), "%" + keyword + "%");
            Predicate firstNamePredicate = criteriaBuilder.like(root.get("firstName"), "%" + keyword + "%");
            Predicate lastNamePredicate = criteriaBuilder.like(root.get("lastName"), "%" + keyword + "%");
            Predicate emailPredicate = criteriaBuilder.like(root.get("email"), "%" + keyword + "%");

            predicates.add(criteriaBuilder.or(usernamePredicate, firstNamePredicate, lastNamePredicate, emailPredicate));
        }

        if (status != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), status));
        }

        // Combine all predicates
        if (!predicates.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        }

        // Apply sorting
        if (!StringUtils.isEmpty(sortBy) && direction != null) {
            if (direction.equals(SortDirection.ASC)) {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortBy)));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortBy)));
            }
        }

        criteriaQuery.select(criteriaBuilder.construct(
                OnDashboardProfileDTO.class,
                root.get("id"),
                root.get("username"),
                root.get("avatarUrl"),
                root.get("firstName"),
                root.get("lastName"),
                root.get("email"),
                root.get("gender"),
                root.get("registerAt")
        ));

        // Fetch the paginated results
        List<OnDashboardProfileDTO> users = entityManager.createQuery(criteriaQuery)
                .setFirstResult((int) pageable.getOffset()) // Starting position
                .setMaxResults(pageable.getPageSize()) // Number of records to fetch
                .getResultList();

        // Fetch the total count
        long total = countUsers(keyword, status);

        return new PageImpl<>(users, pageable, total);
    }

    private long countUsers(String keyword, AccountStatus status) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);

        List<Predicate> countPredicates = new ArrayList<>();

        if (!StringUtils.isEmpty(keyword)) {
            Predicate countUsernamePredicate = criteriaBuilder.like(countRoot.get("username"), "%" + keyword + "%");
            Predicate countFirstNamePredicate = criteriaBuilder.like(countRoot.get("firstName"), "%" + keyword + "%");
            Predicate countLastNamePredicate = criteriaBuilder.like(countRoot.get("lastName"), "%" + keyword + "%");
            Predicate countEmailPredicate = criteriaBuilder.like(countRoot.get("email"), "%" + keyword + "%");

            countPredicates.add(criteriaBuilder.or(countUsernamePredicate, countFirstNamePredicate, countLastNamePredicate, countEmailPredicate));
        }

        if (status != null) {
            countPredicates.add(criteriaBuilder.equal(countRoot.get("status"), status));
        }

        if (!countPredicates.isEmpty()) {
            countQuery.where(criteriaBuilder.and(countPredicates.toArray(new Predicate[0])));
        }

        // Fetch the total count
        countQuery.select(criteriaBuilder.count(countRoot));

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
