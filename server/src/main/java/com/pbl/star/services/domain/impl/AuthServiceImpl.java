package com.pbl.star.services.domain.impl;

import com.pbl.star.dtos.request.user.SignUpParams;
import com.pbl.star.entities.User;
import com.pbl.star.entities.VerificationToken;
import com.pbl.star.enums.AccountStatus;
import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.exceptions.EntityConflictException;
import com.pbl.star.exceptions.IllegalRequestArgumentException;
import com.pbl.star.exceptions.InvalidVerificationTokenException;
import com.pbl.star.mapper.UserSignUpMapper;
import com.pbl.star.repositories.UserRepository;
import com.pbl.star.repositories.VerificationTokenRepository;
import com.pbl.star.services.domain.AuthService;
import com.pbl.star.utils.SignUpValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserSignUpMapper userSignUpMapper;

    @Override
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

    @Override
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

    @Override
    @Transactional
    public User handleResendConfirmation(String userId, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (StringUtils.isBlank(email) || email.equals(user.getEmail())) {
            return user;
        }

        if (!EmailValidator.getInstance().isValid(email)) {
            throw new IllegalRequestArgumentException("Invalid email");
        }

        if (userRepository.existsValidAccountByEmail(email)) {
            throw new EntityConflictException("Email already used by another account");
        }

        user.setEmail(email);
        return userRepository.save(user);
    }
}

