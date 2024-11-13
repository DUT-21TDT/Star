package com.pbl.star.usecase.common.impl;

import com.pbl.star.dtos.query.user.GeneralInformation;
import com.pbl.star.dtos.request.user.SignUpParams;
import com.pbl.star.dtos.response.user.ConfirmSignUpResponse;
import com.pbl.star.dtos.response.user.SignUpResponse;
import com.pbl.star.entities.User;
import com.pbl.star.entities.VerificationToken;
import com.pbl.star.enums.AccountStatus;
import com.pbl.star.events.OnConfirmSignUpEvent;
import com.pbl.star.events.OnSignUpCompleteEvent;
import com.pbl.star.exceptions.EntityConflictException;
import com.pbl.star.services.domain.AuthService;
import com.pbl.star.services.domain.VerificationTokenService;
import com.pbl.star.services.external.EmailService;
import com.pbl.star.usecase.common.AuthUsecase;
import com.pbl.star.utils.AuthUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUsecaseImpl implements AuthUsecase {
    private final AuthService authService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public SignUpResponse signUpByEmail(SignUpParams signUpParams) {
        User savedUser = authService.signUpByEmail(signUpParams);
        eventPublisher.publishEvent(new OnSignUpCompleteEvent(savedUser));

        return SignUpResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .build();
    }

    @Override
    public ConfirmSignUpResponse confirmSignup(String token) {
        User confirmedUser = authService.confirmSignup(token);
        eventPublisher.publishEvent(new OnConfirmSignUpEvent(confirmedUser));

        return ConfirmSignUpResponse.builder()
                .id(confirmedUser.getId())
                .username(confirmedUser.getUsername())
                .email(confirmedUser.getEmail())
                .build();
    }

    @Override
    public void resendConfirmation(String email) {
        GeneralInformation currentUser = AuthUtil.getCurrentUser();

        if (currentUser.getStatus() == AccountStatus.ACTIVE) {
            throw new EntityConflictException("User is already active");
        }

        User user = authService.handleResendConfirmation(currentUser.getId(), email);

        // Remove old verification token
        verificationTokenService.removeVerificationToken(user);
        // Create new verification token
        VerificationToken token = verificationTokenService.createVerificationToken(user);

        try {
            emailService.sendConfirmationEmailAsync(user.getEmail(), token);
        } catch (MailException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
