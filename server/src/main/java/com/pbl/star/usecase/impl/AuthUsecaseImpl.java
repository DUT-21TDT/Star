package com.pbl.star.usecase.impl;

import com.pbl.star.dtos.request.user.SignUpParams;
import com.pbl.star.dtos.response.user.ConfirmSignUpResponse;
import com.pbl.star.dtos.response.user.SignUpResponse;
import com.pbl.star.entities.User;
import com.pbl.star.events.OnConfirmSignUpEvent;
import com.pbl.star.events.OnSignUpCompleteEvent;
import com.pbl.star.services.AuthService;
import com.pbl.star.usecase.AuthUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUsecaseImpl implements AuthUsecase {
    private final AuthService authService;
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
}
