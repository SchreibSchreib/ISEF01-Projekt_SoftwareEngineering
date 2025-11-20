package com.example.backend.security;

import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRateLimiter {

    private final Map<String, List<Long>> attempts = new ConcurrentHashMap<>();
    private static final int MAX_ATTEMPTS = 5; // maximal 5 Versuche
    private static final long TIME_WINDOW = 10 * 60 * 1000; // 10 Minuten in Millisekunden

    public boolean isBlocked(String ip) {
        long now = System.currentTimeMillis();

        attempts.putIfAbsent(ip, new ArrayList<>());
        attempts.get(ip).removeIf(time -> now - time > TIME_WINDOW);

        return attempts.get(ip).size() >= MAX_ATTEMPTS;
    }

    public void recordAttempt(String ip) {
        long now = System.currentTimeMillis();
        attempts.putIfAbsent(ip, new ArrayList<>());
        attempts.get(ip).add(now);
    }

    public void resetAttempts(String ip) {
        attempts.remove(ip);
    }
}