package com.pbl.star.repositories;

import com.pbl.star.models.entities.User;
import com.pbl.star.models.projections.user.BasicUserInfo;
import com.pbl.star.repositories.extensions.UserRepositoryExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, UserRepositoryExtension {
    Boolean existsByUsername(String username);

    @Query("SELECT COUNT(*) > 0 FROM User u WHERE u.email = ?1 AND u.status = 'ACTIVE'")
    Boolean existsValidAccountByEmail(String email);

    @Query("SELECT new com.pbl.star.models.projections.user.BasicUserInfo(u.id, u.username, u.firstName, u.lastName, u.avatarUrl, u.role, u.status, u.password) " +
            "FROM User u " +
            "WHERE u.id = ?1")
    Optional<BasicUserInfo> getGeneralInformationById(String userId);

    @Query("SELECT u.privateProfile FROM User u WHERE u.id = ?1")
    Optional<Boolean> getPrivateProfileById(String userId);
}
