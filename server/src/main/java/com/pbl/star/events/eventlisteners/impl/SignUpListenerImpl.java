package com.pbl.star.events.eventlisteners.impl;

import com.pbl.star.entities.User;
import com.pbl.star.entities.VerificationToken;
import com.pbl.star.events.OnSignUpCompleteEvent;
import com.pbl.star.events.eventlisteners.SignUpListener;
import com.pbl.star.services.AuthService;
import com.pbl.star.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignUpListenerImpl implements SignUpListener {

    private final AuthService authService;
    private final EmailService emailService;

    @Value("${client.web.url}")
    private String clientUrl;

    @Override
    @Async
    @EventListener
    public void onSignUpComplete(OnSignUpCompleteEvent event) {
        try {
            this.confirmRegistration(event);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void confirmRegistration(OnSignUpCompleteEvent event) throws MessagingException {
        User user = event.getUser();
        VerificationToken token = authService.createVerificationToken(user);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl
                = clientUrl + "/confirm-signup?token=" + token.getToken();

        MimeBodyPart confirmationUrlPart = new MimeBodyPart();
        confirmationUrlPart.setText("<a href='" + confirmationUrl + "'>Confirm</a>", "UTF-8", "html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(confirmationUrlPart);

        emailService.sendMail(recipientAddress, subject, multipart);
    }
}
