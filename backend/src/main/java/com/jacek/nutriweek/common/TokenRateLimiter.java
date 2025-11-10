package com.jacek.nutriweek.common;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TokenRateLimiter {

    private final Queue<Long> requests = new ConcurrentLinkedQueue<>();
    private final Long refillTime;
    private final int maxTokens;
    private final AtomicInteger tokens;

    public TokenRateLimiter(int maxTokens, long refillTime){
        this.maxTokens = maxTokens;
        this.tokens = new AtomicInteger(maxTokens);
        this.refillTime = refillTime;
    }

    public int tokens(){
        return tokens.get();
    }

    public boolean tryConsume() {
        refillTokens();
        int current;
        do {
            current = tokens.get();
            if (current <= 0) {
                return false;
            }
        } while (!tokens.compareAndSet(current, current - 1));

        requests.add(System.currentTimeMillis());
        return true;
    }

    private void refillTokens() {
        long now = System.currentTimeMillis();
        Long timestamp;
        while ((timestamp = requests.peek()) != null) {
            if (timestamp + refillTime <= now) {
                requests.poll();
                if (tokens.get() < maxTokens) {
                    tokens.incrementAndGet();
                }
            } else {
                break;
            }
        }
    }
}
