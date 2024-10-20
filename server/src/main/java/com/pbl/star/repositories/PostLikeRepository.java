package com.pbl.star.repositories;

import com.pbl.star.entities.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, String> {
    boolean existsByPostIdAndUserId(String postId, String userId);

    Optional<PostLike> findPostLikeByUserIdAndPostId(String userId, String postId);
}
