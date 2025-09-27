package com.jacek.nutriweek.auth.service;

import com.jacek.nutriweek.auth.repository.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class TokenCleanupService {
    private final TokenRepository tokenRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpiredTokens(){
        tokenRepository.deleteExpired(Instant.now());
    }
}
