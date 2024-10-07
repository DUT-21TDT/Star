package com.pbl.star.services.impl;

import com.pbl.star.dtos.request.user.SignUpParams;
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
import com.pbl.star.utils.SignUpValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

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
        SignUpValidator.validateSignupRequiredFields(signUpParams);

        // Check sign up rules
        SignUpValidator.validateSignUpRules(signUpParams);

        // Check if username already exist
        if (userRepository.existsByUsername(signUpParams.getUsername())) {
            throw new EntityConflictException("Username already exists");
        }

        if (userRepository.existsValidAccountByEmail(signUpParams.getEmail())) {
            throw new EntityConflictException("Email already exists");
        }
    }

    @Transactional
    public User confirmSignup(String token) {
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
        return userRepository.save(user);
    }
}

