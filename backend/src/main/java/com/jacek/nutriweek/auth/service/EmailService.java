package com.jacek.nutriweek.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${app.mail.from}") private String from;
    @Value("${app.urls.verify}") private String verifyUrl;

    public void sendVerificationEmail(String to, String token){
        String link = verifyUrl + "?token=" + token;

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject("Confirm your registration");
        msg.setText("""
            Thanks for signing up!
            Please verify your email by clicking the link below:
            %s
            """.formatted(link));
        mailSender.send(msg);
    }
}
