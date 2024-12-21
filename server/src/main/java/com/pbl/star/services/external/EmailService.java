package com.pbl.star.services.external;

import com.pbl.star.models.entities.VerificationToken;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendConfirmationEmailAsync(String email, VerificationToken token) throws MessagingException;
    void sendConfirmationEmail(String email, VerificationToken token) throws MessagingException;
}
