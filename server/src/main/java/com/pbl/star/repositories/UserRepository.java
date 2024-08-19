package com.pbl.star.repositories;

import com.github.f4b6a3.ulid.Ulid;
import com.pbl.star.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Ulid> {
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
