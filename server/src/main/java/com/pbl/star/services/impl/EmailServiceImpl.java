package com.pbl.star.services.impl;

import com.pbl.star.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendMail(String to, String subject, Multipart content) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        mimeMessage.setRecipient(MimeMessage.RecipientType.TO, InternetAddress.parse(to)[0]);
        mimeMessage.setSubject(subject);
        mimeMessage.setContent(content);

        mailSender.send(mimeMessage);
    }
}
