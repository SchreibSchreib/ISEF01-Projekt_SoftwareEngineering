package com.example.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiRateLimitFilter extends OncePerRequestFilter {

    private final ApiRateLimiter apiRateLimiter;

    public ApiRateLimitFilter(ApiRateLimiter apiRateLimiter) {
        this.apiRateLimiter = apiRateLimiter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("ApiRateLimitFilter AKTIV für: " + path);

        // Login & Session exkludiert
        if (path.equals("/api/login") || path.equals("/api/session/check")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Sanfter Rate-Limit-Schutz
        if (path.startsWith("/api/")) {
            String ip = request.getRemoteAddr();

            if (apiRateLimiter.isRateLimited(ip)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("Zu viele Anfragen. Bitte etwas warten.");
                return;
            }

            apiRateLimiter.recordRequest(ip);
        }

        filterChain.doFilter(request, response);
    }
}
