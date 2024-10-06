package com.pbl.star.repositories;

import com.pbl.star.entities.User;
import com.pbl.star.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {
    Optional<VerificationToken> findByToken(String token);
    List<VerificationToken> findByUser(User user);
    void deleteAllByUser(User user);
}
