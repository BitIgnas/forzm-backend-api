package org.forzm.demo.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.forzm.demo.service.MailContentBuilder;
import org.forzm.demo.service.MailSendingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailSendingServiceImpl implements MailSendingService {

    private final JavaMailSender javaMailSender;
    private final MailContentBuilder mailContentBuilder;

//    @Value("${app.verification.mail.url}")
//    private String url;

    @Override
    public void sendVerificationMail(String to, String token) {
        String link = "http://localhost:8080/api/verify/" +  token;
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

        try {
            messageHelper.setTo(to);
            messageHelper.setFrom("ignotas.tunaitis03@gmail.com");
            messageHelper.setSubject("Welcome to FORUM-BAY. Please activate your account");
            messageHelper.setText(mailContentBuilder.build(link), true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
