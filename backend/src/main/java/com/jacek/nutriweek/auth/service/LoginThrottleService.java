package com.jacek.nutriweek.auth.service;

import com.jacek.nutriweek.common.TokenRateLimiter;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class LoginThrottleService {

    private final ConcurrentHashMap<String, TokenRateLimiter> limiters = new ConcurrentHashMap<>();

    private TokenRateLimiter getLimiter(String key){
        return limiters.computeIfAbsent(key,
                k -> new TokenRateLimiter(10, TimeUnit.MINUTES.toMillis(10)));
    }

    public boolean allowLogin(String key){
        TokenRateLimiter limiter = getLimiter(key);
        return limiter.tryConsume();
    }

    public void resetLimiter(String key){
        limiters.remove(key);
    }
}
