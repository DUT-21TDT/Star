package com.pbl.star.events.eventlisteners.impl;

import com.pbl.star.events.OnConfirmSignUpEvent;
import com.pbl.star.events.eventlisteners.ConfirmSignUpListener;
import com.pbl.star.services.domain.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfirmSignUpListenerImpl implements ConfirmSignUpListener {

    private final VerificationTokenService verificationTokenService;

    @Override
    @Async
    @EventListener
    public void removeVerificationToken(OnConfirmSignUpEvent event) {
        verificationTokenService.removeVerificationToken(event.getUser());
    }
}
