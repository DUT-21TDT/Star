package com.pbl.star.repositories;

import com.pbl.star.entities.Post;
import com.pbl.star.repositories.extensions.PostRepositoryExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, String>, PostRepositoryExtension {
}
