package com.jacek.nutriweek.auth.controller;

import com.jacek.nutriweek.auth.dto.LoginRequest;
import com.jacek.nutriweek.auth.dto.RegisterRequest;
import com.jacek.nutriweek.auth.service.AuthService;
import com.jacek.nutriweek.auth.service.LoginThrottleService;
import com.jacek.nutriweek.auth.service.VerificationResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Locale;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authManager;
    private final SecurityContextRepository securityContextRepository;
    private final LoginThrottleService loginThrottleService;

    @Value("${app.urls.frontendVerified}") String verifyRedirect;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/verify")
    public RedirectView verifyEmail(@RequestParam String token) {
        VerificationResult result = authService.verify(token);

        String status = switch (result) {
            case SUCCESS -> "success";
            case EXPIRED -> "expired";
            case INVALID -> "invalid";
            case ALREADY_VERIFIED -> "already";
        };

        return new RedirectView(verifyRedirect + "?status=" + status);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        String key = loginRequest.username().toLowerCase(Locale.ROOT);

        if(!loginThrottleService.allowLogin(key)){
            log.info("Too many login requests for {}", key);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many login attempts. Try again later");
        }

        try {
            Authentication auth = authManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequest.username(),
                                    loginRequest.password())
                    );

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);

            securityContextRepository.saveContext(context, request, response);

            loginThrottleService.resetLimiter(key);
            return ResponseEntity.ok(Map.of("message", "Login successful", "redirect", "/menus"));
        }
        catch (AuthenticationException e) {
            log.warn("Failed login for user {}", loginRequest.username());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> currentUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(Map.of(
                "username", auth.getName(),
                "roles", auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        ));
    }
    @GetMapping("/csrf")
    public ResponseEntity<CsrfToken> csrf(CsrfToken token) {
        return ResponseEntity.ok().body(token);
    }
}
