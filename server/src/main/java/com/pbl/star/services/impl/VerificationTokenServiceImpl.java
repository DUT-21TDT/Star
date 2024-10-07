package com.pbl.star.services.impl;

import com.pbl.star.entities.User;
import com.pbl.star.entities.VerificationToken;
import com.pbl.star.repositories.VerificationTokenRepository;
import com.pbl.star.services.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public VerificationToken createVerificationToken(User user) {
        return verificationTokenRepository.save(VerificationToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(VerificationToken.EXPIRATION_SECOND))
                .build());
    }

    @Override
    public void removeVerificationToken(User user) {
        verificationTokenRepository.deleteAllByUser(user);
    }
}
