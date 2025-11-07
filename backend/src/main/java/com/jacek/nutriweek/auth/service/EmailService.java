package com.jacek.nutriweek.auth.service;

import com.jacek.nutriweek.common.exception.VerificationMailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${app.mail.from}") private String from;
    @Value("${app.urls.verify}") private String verifyUrl;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public void sendVerificationEmail(String to, String token){
        if(!isValidEmail(to)){
            log.error("Invalid email format: {}", to);
            throw new IllegalArgumentException("Email address invalid or empty");
        }

        if(!isValidUUID(token)){
            log.error("Invalid UUID token format: {}", token);
            throw new IllegalArgumentException("Token is not valid UUID or empty");
        }

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

        try {
            mailSender.send(msg);
        } catch (MailException e){
            log.error("Email send failed for {}: {}", to, e.getMessage());
            throw new VerificationMailException("Verification mail send failed, please try again later.");
        }
    }

    private boolean isValidEmail(String email){
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    private boolean isValidUUID(String token) {
        if (token == null) return false;
        try {
            UUID.fromString(token);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
