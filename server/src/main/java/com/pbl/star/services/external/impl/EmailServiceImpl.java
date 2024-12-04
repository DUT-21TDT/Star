package com.pbl.star.services.external.impl;

import com.pbl.star.entities.VerificationToken;
import com.pbl.star.services.external.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${client.web.url}")
    private String clientUrl;

    @Async
    @Override
    public void sendConfirmationEmailAsync(String email, VerificationToken token) throws MessagingException {
         sendConfirmationEmail(email, token);
    }

    @Override
    public void sendConfirmationEmail(String email, VerificationToken token) throws MessagingException, MailSendException {
        String subject = "Registration Confirmation";
        String confirmationUrl
                = clientUrl + "/confirm-signup?token=" + token.getToken();

        MimeBodyPart confirmationUrlPart = new MimeBodyPart();
        confirmationUrlPart.setText("<a href='" + confirmationUrl + "'>Confirm</a>", "UTF-8", "html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(confirmationUrlPart);

        sendMail(email, subject, multipart);
    }

//    @Override
//    public void sendBlockEmail(String email) throws MessagingException {
//        String subject = "Account Blocked";
//        String content = "Your account has been blocked. Please contact the administrator for more information.";
//
//        MimeBodyPart contentPart = new MimeBodyPart();
//        contentPart.setText(content, "UTF-8", "html");
//        Multipart multipart = new MimeMultipart();
//        multipart.addBodyPart(contentPart);
//
//        sendMail(email, subject, multipart);
//    }

    private void sendMail(String to, String subject, Multipart content) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        mimeMessage.setRecipient(MimeMessage.RecipientType.TO, InternetAddress.parse(to)[0]);
        mimeMessage.setSubject(subject);
        mimeMessage.setContent(content);

        mailSender.send(mimeMessage);
    }
}
