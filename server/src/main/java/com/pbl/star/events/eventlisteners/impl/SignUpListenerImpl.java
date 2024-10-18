package com.pbl.star.events.eventlisteners.impl;

import com.pbl.star.entities.User;
import com.pbl.star.entities.VerificationToken;
import com.pbl.star.events.OnSignUpCompleteEvent;
import com.pbl.star.events.eventlisteners.SignUpListener;
import com.pbl.star.services.domain.VerificationTokenService;
import com.pbl.star.services.external.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SignUpListenerImpl implements SignUpListener {

    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    @Override
    @Async
    @EventListener
    public void sendConfirmationEmail(OnSignUpCompleteEvent event) {
        try {
            User user = event.getUser();
            VerificationToken token = verificationTokenService.createVerificationToken(user);
            emailService.sendConfirmationEmail(user.getEmail(), token);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
