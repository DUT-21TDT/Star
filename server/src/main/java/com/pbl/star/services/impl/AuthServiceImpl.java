package com.pbl.star.services.impl;

import com.pbl.star.dtos.request.SignUpParams;
import com.pbl.star.dtos.response.ConfirmSignUpResponse;
import com.pbl.star.entities.User;
import com.pbl.star.entities.VerificationToken;
import com.pbl.star.enums.AccountStatus;
import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.exceptions.EntityConflictException;
import com.pbl.star.exceptions.InvalidVerificationTokenException;
import com.pbl.star.mapper.UserSignUpMapper;
import com.pbl.star.repositories.UserRepository;
import com.pbl.star.repositories.VerificationTokenRepository;
import com.pbl.star.services.AuthService;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserSignUpMapper userSignUpMapper;

    @Transactional
    public User signUpByEmail(SignUpParams signUpParams) {

        // Validate sign up information
        validateSignUpInformation(signUpParams);

        User newUser = userSignUpMapper.toEntity(signUpParams);
        newUser.setRegisterAt(Instant.now());

        return userRepository.save(newUser);
    }

    private void validateSignUpInformation(SignUpParams signUpParams) {
        // Check required fields
        AuthUtil.validateSignupRequiredFields(signUpParams);

        // Check sign up rules
        AuthUtil.validateSignUpRules(signUpParams);

        // Check if username already exist
        if (userRepository.existsByUsername(signUpParams.getUsername())) {
            throw new EntityConflictException("Username already exists");
        }

        if (userRepository.existsValidAccountByEmail(signUpParams.getEmail())) {
            throw new EntityConflictException("Email already exists");
        }
    }

    @Transactional
    public ConfirmSignUpResponse confirmSignup(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Token not found"));

        if (verificationToken.getExpiryDate().isBefore(Instant.now())) {
            throw new InvalidVerificationTokenException("Token expired");
        }

        User user = verificationToken.getUser();

        if (user.getStatus() == AccountStatus.ACTIVE) {
            throw new InvalidVerificationTokenException("Token is used");
        }

        user.setStatus(AccountStatus.ACTIVE);
        userRepository.save(user);

        // Remove all inactive account with the same email
        userRepository.deleteAll(userRepository.findByEmailAndStatus(user.getEmail(), AccountStatus.INACTIVE));

        return new ConfirmSignUpResponse(user.getId(), user.getUsername(), user.getEmail());
    }

    @Override
    public VerificationToken createVerificationToken(User user) {
        return verificationTokenRepository.save(VerificationToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(VerificationToken.EXPIRATION_SECOND))
                .build());
    }
}

