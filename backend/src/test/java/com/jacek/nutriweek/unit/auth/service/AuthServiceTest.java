package com.jacek.nutriweek.unit.auth.service;

import com.jacek.nutriweek.auth.dto.RegisterRequest;
import com.jacek.nutriweek.auth.entity.VerificationToken;
import com.jacek.nutriweek.auth.repository.TokenRepository;
import com.jacek.nutriweek.auth.service.AuthService;
import com.jacek.nutriweek.auth.service.EmailService;
import com.jacek.nutriweek.common.exception.UserAlreadyExistsException;
import com.jacek.nutriweek.user.entity.User;
import com.jacek.nutriweek.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock UserRepository userRepository;
    @Mock TokenRepository tokenRepository;
    @Mock EmailService emailService;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks
    AuthService authService;

    private static final String USERNAME = "john123";
    private static final String EMAIL = "john@example.com";
    private static final String PASSWORD = "password";

    @Test
    void shouldRegisterUser_whenValidRequest() {
        RegisterRequest req = new RegisterRequest(USERNAME, EMAIL, PASSWORD);
        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn("encoded");

        when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        authService.register(req);

        verify(userRepository).save(any(User.class));
        verify(tokenRepository).save(any(VerificationToken.class));
        verify(emailService).sendVerificationEmail(eq(EMAIL), anyString());
    }

    @Test
    void shouldThrow_whenUsernameAlreadyExists(){
        RegisterRequest req = new RegisterRequest(USERNAME, EMAIL, PASSWORD);
        when(userRepository.existsByUsername(USERNAME)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(req));

        verify(userRepository, never()).save(any());
        verify(tokenRepository, never()).save(any());
        verify(emailService, never()).sendVerificationEmail(anyString(), anyString());
    }

    @Test
    void shouldThrow_whenEmailAlreadyExists(){
        RegisterRequest req = new RegisterRequest(USERNAME, EMAIL, PASSWORD);
        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(req));

        verify(userRepository, never()).save(any());
        verify(tokenRepository, never()).save(any());
        verify(emailService, never()).sendVerificationEmail(anyString(), anyString());
    }

    @Test
    void shouldEncodePasswordBeforeSaving() {
        RegisterRequest req = new RegisterRequest(USERNAME, EMAIL, PASSWORD);
        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn("encoded");

        when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        authService.register(req);

        verify(userRepository).save(argThat(user -> user.getPassword().equals("encoded")));
    }
    @Test
    void shouldAssignUserRole() {
        RegisterRequest req = new RegisterRequest(USERNAME, EMAIL, PASSWORD);
        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn("encoded");
        when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        authService.register(req);

        verify(userRepository).save(argThat(user -> user.getRoles().contains("ROLE_USER")));
    }
    @Test
    void shouldGenerateVerificationTokenForUser() {
        RegisterRequest req = new RegisterRequest(USERNAME, EMAIL, PASSWORD);
        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn("encoded");

        User savedUser = new User(USERNAME, "encoded", EMAIL, false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        authService.register(req);

        verify(tokenRepository).save(argThat(token -> token.getUser().equals(savedUser)));
    }
    @Test
    void shouldSendVerificationEmailWithCorrectArgs(){
        RegisterRequest req = new RegisterRequest(USERNAME, EMAIL, PASSWORD);
        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn("encoded");

        when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        when(tokenRepository.save(any(VerificationToken.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        authService.register(req);

        ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);

        verify(emailService).sendVerificationEmail(argThat(
                email->email.equals(EMAIL)),
                tokenCaptor.capture());

        assertNotNull(tokenCaptor.getValue());
        assertDoesNotThrow(() -> UUID.fromString(tokenCaptor.getValue()),
                "Token should be a valid UUID string");
    }

    @Test
    void shouldVerifyUser_whenValidToken() {
        User user = new User("john", "encoded", "john@example.com", false);
        VerificationToken vt = new VerificationToken(UUID.randomUUID().toString(), user);
        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(vt));

        boolean result = authService.verify(vt.getToken());

        assertTrue(result);
        assertTrue(user.isEnabled());

        verify(userRepository).save(argThat(u -> u.equals(user) && u.isEnabled()));
        verify(tokenRepository).delete(vt);
    }

    @Test
    void shouldReturnFalse_whenInvalidToken(){
        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        boolean result = authService.verify("non_existent");

        assertFalse(result);
        verify(userRepository, never()).save(any());
        verify(tokenRepository, never()).delete(any());
    }

    @Test
    void shouldReturnFalse_whenTokenExpired(){
        User user = new User(USERNAME, "encoded", EMAIL, false);
        VerificationToken vt = new VerificationToken(UUID.randomUUID().toString(), user);
        vt.setExpirationDate(Instant.now().minusSeconds(60));

        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(vt));

        boolean result = authService.verify(vt.getToken());

        assertFalse(result);
        assertFalse(user.isEnabled());

        verify(userRepository, never()).save(any());
        verify(tokenRepository, never()).delete(any());
    }
}
