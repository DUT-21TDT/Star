package com.pbl.star.services.domain.impl;

import com.pbl.star.models.entities.User;
import com.pbl.star.models.entities.VerificationToken;
import com.pbl.star.repositories.VerificationTokenRepository;
import com.pbl.star.services.domain.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    @Transactional
    public VerificationToken createVerificationToken(User user) {
        return verificationTokenRepository.save(VerificationToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(VerificationToken.EXPIRATION_SECOND))
                .build());
    }

    @Override
    @Transactional
    public void removeVerificationToken(User user) {
        verificationTokenRepository.deleteAllByUser(user);
    }
}
