package com.pbl.star.services;

import com.github.f4b6a3.ulid.UlidCreator;
import com.pbl.star.dtos.request.SignUpParams;
import com.pbl.star.dtos.response.SignUpResponse;
import com.pbl.star.entities.User;
import com.pbl.star.enums.AccountStatus;
import com.pbl.star.enums.UserRole;
import com.pbl.star.exceptions.UserAlreadyExistException;
import com.pbl.star.repositories.UserRepository;
import com.pbl.star.utils.AuthUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public SignUpResponse signUpByEmail(SignUpParams signUpParams) {

        // Validate sign up information
        validateSignUpInformation(signUpParams);

        // Handle password hashing
        String hashedPassword = passwordEncoder.encode(signUpParams.getPassword());

        // Handle date of birth parsing
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate parsed_DateOfBirth = LocalDate.parse(signUpParams.getDateOfBirth(), dateTimeFormatter);

        User newUser = User.builder()
                .id(UlidCreator.getUlid().toString())
                .username(signUpParams.getUsername())
                .role(UserRole.USER)
                .registerAt(Instant.now())
                .status(AccountStatus.ACTIVE)
                .email(signUpParams.getEmail())
                .password(hashedPassword)
                .firstName(signUpParams.getFirstName())
                .lastName(signUpParams.getLastName())
                .dateOfBirth(parsed_DateOfBirth)
                .gender(signUpParams.getGender())
                .privateProfile(false)
                .build();

        userRepository.save(newUser);

        return new SignUpResponse(newUser.getId(), newUser.getUsername());
    }

    private void validateSignUpInformation(SignUpParams signUpParams) {
        // Check required fields
        AuthUtil.validateSignupRequiredFields(signUpParams);

        // Check confirm password
        AuthUtil.validateConfirmPassword(signUpParams.getPassword(), signUpParams.getConfirmPassword());

        // Check if username and email already exist
        if (userRepository.existsByUsername(signUpParams.getUsername())) {
            throw new UserAlreadyExistException("Username already exists");
        }

        // TODO: Check if email is valid

        if (userRepository.existsByEmail(signUpParams.getEmail())) {
            throw new UserAlreadyExistException("Email already exists");
        }
    }
}
