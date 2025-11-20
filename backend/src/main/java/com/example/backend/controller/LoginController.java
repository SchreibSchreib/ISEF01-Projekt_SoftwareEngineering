package com.example.backend.controller;

import com.example.backend.dto.LoginRequest;
import com.example.backend.models.User;
import com.example.backend.security.LoginRateLimiter;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginRateLimiter rateLimiter;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest http) {
        String ip = http.getRemoteAddr();

        if (rateLimiter.isBlocked(ip)) {
            return ResponseEntity.status(429)
                    .body(Map.of("message",
                            "Zu viele Fehlversuche. Bitte versuchen Sie es später erneut."));
        }

        User user = userService.getUserByNameAndPassword(request.getName(), request.getPasswort());

        if (user == null) {
            rateLimiter.recordAttempt(ip);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Ungültige Anmeldedaten"));
        }

        rateLimiter.resetAttempts(ip);

        http.getSession(true).setAttribute("userId", user.getUserId());

        return ResponseEntity.ok(Map.of("message", "Login erfolgreich"));
    }
}
