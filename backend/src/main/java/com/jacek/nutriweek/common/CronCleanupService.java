package com.jacek.nutriweek.common;

import com.jacek.nutriweek.auth.repository.TokenRepository;
import com.jacek.nutriweek.user.entity.User;
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
public class CronCleanupService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void oncePerDay(){
        deleteExpiredTokens();
        deleteOldDemoUsers();
    }

    private void deleteExpiredTokens(){
        Instant now = Instant.now();
        List<User> expiredUsers = tokenRepository.findExpired(now);

        if (!expiredUsers.isEmpty()) {
            userRepository.deleteAll(expiredUsers);
            int expiredTokens = tokenRepository.deleteExpired(now);
            log.info("Scheduled cleanup: {} users and tokens removed", expiredTokens);
        }
    }

    private void deleteOldDemoUsers(){
        Instant expireTime = Instant.now().minusSeconds(86400);
        List<User> expiredUsers = userRepository.findExpiredDemoUsers(expireTime);

        if (!expiredUsers.isEmpty()){
            userRepository.deleteAll(expiredUsers);
            log.info("Scheduled cleanup: {} demo users removed ", expiredUsers.size());
        }
    }
}
