package com.jacek.nutriweek.auth.controller;

import com.jacek.nutriweek.auth.dto.RegisterRequest;
import com.jacek.nutriweek.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Value("${app.urls.frontendVerified}") String okRedirect;
    @Value("${app.urls.frontendFailed}")   String failRedirect;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/verify")
    public RedirectView verify(@RequestParam String token, HttpServletResponse response) throws IOException {
        boolean ok = authService.verify(token, response);
        return new RedirectView(ok ? okRedirect : failRedirect);
    }

}
