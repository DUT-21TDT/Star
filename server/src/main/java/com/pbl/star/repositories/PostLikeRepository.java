package com.pbl.star.repositories;

import com.pbl.star.entities.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, String> {
    boolean existsByPostIdAndUserId(String postId, String userId);

    Optional<PostLike> findPostLikeByUserIdAndPostId(String userId, String postId);
}
