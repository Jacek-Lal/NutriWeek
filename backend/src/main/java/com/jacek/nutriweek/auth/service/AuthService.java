package com.jacek.nutriweek.auth.service;

import com.jacek.nutriweek.auth.dto.RegisterRequest;
import com.jacek.nutriweek.auth.entity.VerificationToken;
import com.jacek.nutriweek.auth.repository.TokenRepository;
import com.jacek.nutriweek.common.exception.ResourceNotFoundException;
import com.jacek.nutriweek.common.exception.UserAlreadyExistsException;
import com.jacek.nutriweek.user.entity.User;
import com.jacek.nutriweek.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.login())) {
            log.warn("User with username {} already exists", req.login());
            throw new UserAlreadyExistsException("Username already taken");
        }
        if (userRepository.existsByEmail(req.email())) {
            log.warn("User with email {} already exists", req.email());
            throw new UserAlreadyExistsException("Email already registered");
        }

        User newUser = new User(req.login(),
                passwordEncoder.encode(req.password()),
                req.email(),
                false);
        newUser.getRoles().add("ROLE_USER");

        User user = userRepository.save(newUser);

        log.info("Created new user (login: {} email: {})", user.getUsername(), user.getEmail());

        String token = UUID.randomUUID().toString();
        VerificationToken vt = new VerificationToken(token, user);
        tokenRepository.save(vt);

        emailService.sendVerificationEmail(user.getEmail(), token);

        log.info("Verification email sent to {}", user.getEmail());
    }

    public VerificationResult verify(String token) {
        VerificationToken vt = tokenRepository.findByToken(token).orElse(null);

        if (vt == null) {
            log.warn("Verification failed: token {} not found", token);
            return VerificationResult.INVALID;
        }

        if (vt.getExpirationDate().isBefore(Instant.now())) {
            log.warn("Verification failed: token {} expired", token);
            return VerificationResult.EXPIRED;
        }

        User user = vt.getUser();
        if(user.isEnabled()){
            log.info("User {} already verified", user.getUsername());
            tokenRepository.delete(vt);
            return VerificationResult.ALREADY_VERIFIED;
        }

        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.delete(vt);

        log.info("User {} verified successfully", user.getUsername());

        return VerificationResult.SUCCESS;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles().toArray(new String[0]))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();
    }
}
