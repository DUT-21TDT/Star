package com.pbl.star.repositories;

import com.pbl.star.entities.Post;
import com.pbl.star.repositories.extensions.PostRepositoryExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, String>, PostRepositoryExtension {
    @Query("SELECT p FROM Post p WHERE p.id = :id AND p.isDeleted = false")
    Optional<Post> findExistPostById(String id);

    @Query("SELECT COUNT(*) FROM Post p WHERE p.parentPostId = :postId AND p.isDeleted = false")
    int countExistRepliesOfPost(String postId);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM post p WHERE p.post_id=?1 AND p.is_deleted=?2)", nativeQuery = true)
    boolean existsByIdAndDeleted(String postId, boolean isDeleted);
}
