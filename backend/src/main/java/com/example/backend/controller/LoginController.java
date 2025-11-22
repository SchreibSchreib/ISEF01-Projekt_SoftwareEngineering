package com.example.backend.controller;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.TurnstileResponse;
import com.example.backend.models.User;
import com.example.backend.security.LoginRateLimiter;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginRateLimiter loginLimiter;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${turnstile.secret-key}")
    private String turnstileSecret;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest http) {

        String ip = http.getRemoteAddr();

        // Login Limit prüfen
        if (loginLimiter.isBlocked(ip)) {
            return ResponseEntity.status(429)
                    .body(Map.of("message", "Zu viele Fehlversuche. Bitte versuchen Sie es später erneut."));
        }

        // Captcha prüfen
        if (!verifyCaptcha(request.getCaptchaToken())) {
            return ResponseEntity.status(401)
                    .body(Map.of("message", "Ungültiges Captcha."));
        }

        // Benutzer prüfen
        User user = userService.getUserByNameAndPassword(request.getName(), request.getPasswort());

        if (user == null) {
            loginLimiter.recordAttempt(ip);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Ungültige Anmeldedaten"));
        }

        // loginLimiter zurücksetzen
        loginLimiter.resetAttempts(ip);

        // Session setzen
        http.getSession(true).setAttribute("userId", user.getUserId());

        return ResponseEntity.ok(Map.of("message", "Login erfolgreich"));
    }

    private boolean verifyCaptcha(String token) {

        if (token == null || token.isBlank()) {
            return false;
        }

        String url = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("secret", turnstileSecret);
        data.add("response", token);

        TurnstileResponse result = restTemplate.postForObject(url, data, TurnstileResponse.class);

        return result != null && result.isSuccess();
    }
}
