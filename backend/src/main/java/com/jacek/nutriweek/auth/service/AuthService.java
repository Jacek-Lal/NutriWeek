package com.jacek.nutriweek.auth.service;

import com.jacek.nutriweek.auth.dto.RegisterRequest;
import com.jacek.nutriweek.auth.entity.VerificationToken;
import com.jacek.nutriweek.auth.repository.TokenRepository;
import com.jacek.nutriweek.common.exception.UserAlreadyExistsException;
import com.jacek.nutriweek.user.entity.User;
import com.jacek.nutriweek.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;



    public void register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.login())) {
            throw new UserAlreadyExistsException("Username already taken");
        }
        if (userRepository.existsByEmail(req.email())) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        User newUser = new User(req.login(),
                passwordEncoder.encode(req.password()),
                req.email(),
                false);
        newUser.getRoles().add("ROLE_USER");

        User user = userRepository.save(newUser);

        String token = UUID.randomUUID().toString();
        VerificationToken vt = new VerificationToken(token, user);
        tokenRepository.save(vt);

        emailService.sendVerificationEmail(user.getEmail(), token);
    }

    public boolean verify(String token, HttpServletResponse response) {
        VerificationToken vt = tokenRepository.findByToken(token).orElse(null);

        if (vt ==  null || vt.getExpirationDate().isBefore(Instant.now()))
            return false;

        User user = vt.getUser();
        user.setEnabled(true);

        userRepository.save(user);
        tokenRepository.delete(vt);

        return true;
    }
}
