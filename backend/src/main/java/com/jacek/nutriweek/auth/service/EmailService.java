package com.jacek.nutriweek.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sibApi.TransactionalEmailsApi;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${app.mail.api-key}") private String apiKey;
    @Value("${app.mail.sender-email}") private String senderEmail;
    @Value("${app.mail.sender-name}") private String senderName;
    @Value("${app.urls.verify}") private String verifyUrl;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public void sendVerificationEmail(String to, String token) {
        if(!isValidEmail(to)){
            log.error("Invalid email format: {}", to);
            throw new IllegalArgumentException("Email address invalid or empty");
        }

        if(!isValidUUID(token)){
            log.error("Invalid UUID token format: {}", token);
            throw new IllegalArgumentException("Token is not valid UUID or empty");
        }
        String link = verifyUrl + "?token=" + token;
        String subject = "NutriWeek verification";
        String htmlContent = """
            <html>
                <body>
                    <p>Thanks for signing up!</p>
                    <p>Please verify your email by clicking the link below:</p>
                    <a href="%s">%s</a>
                </body>
            </html>
            """.formatted(link, link);

        ApiClient client = Configuration.getDefaultApiClient();
        client.setApiKey(apiKey);

        TransactionalEmailsApi api = new TransactionalEmailsApi(client);

        SendSmtpEmail email = new SendSmtpEmail()
                .sender(new SendSmtpEmailSender().email(senderEmail).name(senderName))
                .to(List.of(new SendSmtpEmailTo().email(to)))
                .subject(subject)
                .htmlContent(htmlContent);

        try {
            api.sendTransacEmail(email);
        } catch (ApiException e) {
            log.error("Email send failed for {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email: " + e.getMessage());
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
