package com.pbl.star.usecase.impl;

import com.pbl.star.dtos.request.user.SignUpParams;
import com.pbl.star.dtos.response.user.ConfirmSignUpResponse;
import com.pbl.star.dtos.response.user.SignUpResponse;
import com.pbl.star.entities.User;
import com.pbl.star.entities.VerificationToken;
import com.pbl.star.events.OnConfirmSignUpEvent;
import com.pbl.star.events.OnSignUpCompleteEvent;
import com.pbl.star.services.domain.AuthService;
import com.pbl.star.services.domain.VerificationTokenService;
import com.pbl.star.services.external.EmailService;
import com.pbl.star.usecase.AuthUsecase;
import com.pbl.star.utils.AuthUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
        String currentUserId = AuthUtil.getCurrentUser().getId();
        User user = authService.handleResendConfirmation(currentUserId, email);

        // Remove old verification token
        verificationTokenService.removeVerificationToken(user);
        // Create new verification token
        VerificationToken token = verificationTokenService.createVerificationToken(user);

        try {
            emailService.sendConfirmationEmailAsync(user.getEmail(), token);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
