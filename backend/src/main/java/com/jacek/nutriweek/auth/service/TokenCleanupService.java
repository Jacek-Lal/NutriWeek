package com.jacek.nutriweek.auth.service;

import com.jacek.nutriweek.auth.repository.TokenRepository;
import com.jacek.nutriweek.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class TokenCleanupService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpiredTokens(){
        Instant now = Instant.now();
        List<Long> expiredUserIds = tokenRepository.findExpired(now);

        if (!expiredUserIds.isEmpty()) {
            userRepository.deleteByIdIn(expiredUserIds);
            int expired = tokenRepository.deleteExpired(now);
            log.info("Scheduled cleanup: {} users and tokens removed", expired);
        }
    }
}
