package com.jacek.nutriweek.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock JavaMailSender mailSender;
    @InjectMocks EmailService emailService;

    private final String from = "no-reply@nutriweek.com";
    private final String verifyUrl = "http://localhost:8080/auth/verify";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "from", from);
        ReflectionTestUtils.setField(emailService, "verifyUrl", verifyUrl);
    }

    @Test
    void shouldSendEmail_whenValidEmailAndToken(){
        String email = "john@example.com";
        String token = "123e4567-e89b-42d3-a456-556642440000";

        emailService.sendVerificationEmail(email, token);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage msg = captor.getValue();

        assertNotNull(msg.getFrom());
        assertNotNull(msg.getTo());
        assertNotNull(msg.getSubject());
        assertNotNull(msg.getText());

        assertEquals(email, msg.getTo()[0]);
        assertEquals(from, msg.getFrom());

        assertTrue(msg.getText().contains(token));
        assertTrue(msg.getText().contains(verifyUrl));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ",  "john", "john@", "john@example"})
    @NullSource
    void shouldThrow_whenEmailInvalid(String invalidEmail){
        String token = "123e4567-e89b-42d3-a456-556642440000";

        assertThrows(IllegalArgumentException.class,
                () -> emailService.sendVerificationEmail(invalidEmail, token));
    }
    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "abc", "123-456-789-000"})
    @NullSource
    void shouldThrow_whenTokenInvalid(String invalidToken){
        String email = "john@example.com";

        assertThrows(IllegalArgumentException.class,
                () -> emailService.sendVerificationEmail(email, invalidToken));
    }
}