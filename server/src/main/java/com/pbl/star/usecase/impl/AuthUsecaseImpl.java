package com.pbl.star.usecase.impl;

import com.pbl.star.dtos.request.auth.SignUpParams;
import com.pbl.star.dtos.response.auth.ConfirmSignUpResponse;
import com.pbl.star.dtos.response.auth.SignUpResponse;
import com.pbl.star.entities.User;
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
        return new SignUpResponse(savedUser.getId(), savedUser.getUsername());
    }

    @Override
    public ConfirmSignUpResponse confirmSignup(String token) {
        return authService.confirmSignup(token);
    }
}
