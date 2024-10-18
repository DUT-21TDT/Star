package com.pbl.star.repositories;

import com.pbl.star.dtos.query.user.GeneralInformation;
import com.pbl.star.entities.User;
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

    Optional<GeneralInformation> getGeneralInformationById(String userId);
}
