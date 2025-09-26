package com.jacek.nutriweek.service;

import com.jacek.nutriweek.dto.auth.RegisterRequest;
import com.jacek.nutriweek.exceptions.UserAlreadyExistsException;
import com.jacek.nutriweek.model.User;
import com.jacek.nutriweek.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.login())) {
            throw new UserAlreadyExistsException("Username already taken");
        }
        if (userRepository.existsByEmail(req.email())) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        User user = new User(req.login(),
                passwordEncoder.encode(req.password()),
                req.email(),
                true);

        userRepository.save(user);
    }
}
