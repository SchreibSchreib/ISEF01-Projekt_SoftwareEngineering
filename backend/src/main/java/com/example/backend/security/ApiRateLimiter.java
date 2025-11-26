package com.example.backend.security;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class ApiRateLimiter {

    private static final int MAX_REQUESTS = 300; // Schwellenwert
    private static final long TIME_WINDOW = 10 * 60 * 1000;

    private final Map<String, ConcurrentLinkedDeque<Long>> requestTimes = new ConcurrentHashMap<>();

    public boolean isRateLimited(String ip) {
        long now = System.currentTimeMillis();

        requestTimes.putIfAbsent(ip, new ConcurrentLinkedDeque<>());
        ConcurrentLinkedDeque<Long> deque = requestTimes.get(ip);

        // Alte Einträge entfernen
        while (!deque.isEmpty() && now - deque.peekFirst() > TIME_WINDOW) {
            deque.pollFirst();
        }

        return deque.size() >= MAX_REQUESTS;
    }

    public void recordRequest(String ip) {
        long now = System.currentTimeMillis();

        requestTimes.putIfAbsent(ip, new ConcurrentLinkedDeque<>());
        requestTimes.get(ip).addLast(now);
    }
}

