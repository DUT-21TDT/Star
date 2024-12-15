package com.pbl.star.repositories;

import com.pbl.star.entities.PostRepost;
import com.pbl.star.repositories.extensions.PostRepostRepositoryExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepostRepository extends JpaRepository<PostRepost, String>, PostRepostRepositoryExtension {
    boolean existsByPostIdAndUserId(String postId, String userId);
    Optional<PostRepost> findPostRepostByPostIdAndUserId(String postId, String userId);
    Long countPostRepostsByPostId(String postId);
}
