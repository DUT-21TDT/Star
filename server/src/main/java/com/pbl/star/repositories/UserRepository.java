package com.pbl.star.repositories;

import com.pbl.star.entities.User;
import com.pbl.star.enums.AccountStatus;
import com.pbl.star.enums.UserRole;
import com.pbl.star.repositories.extensions.UserRepositoryExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String>, UserRepositoryExtension {
    Boolean existsByUsername(String username);

    @Query("SELECT COUNT(u.id) > 0 FROM User u WHERE u.email = ?1 AND u.status != 'INACTIVE'")
    Boolean existsValidAccountByEmail(String email);

    List<User> findByEmailAndStatus(String email, AccountStatus accountStatus);

    @Query("SELECT u.role FROM User u WHERE u.id = ?1")
    UserRole getRoleByUserId(String userId);
}
