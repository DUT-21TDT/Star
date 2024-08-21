package com.pbl.star.services;

import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;

public interface EmailService {
    void sendMail(String to, String subject, Multipart content) throws MessagingException;
}
