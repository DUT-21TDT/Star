package com.pbl.star.repositories;

import com.github.f4b6a3.ulid.Ulid;
import com.pbl.star.entities.User;
import com.pbl.star.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String> {
    Boolean existsByUsername(String username);

    @Query("SELECT COUNT(u.id) > 0 FROM User u WHERE u.email = ?1 AND u.status != 'INACTIVE'")
    Boolean existsValidAccountByEmail(String email);
}
