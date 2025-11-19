package com.example.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @GetMapping("/check")
    public ResponseEntity<?> checkSession(HttpServletRequest request) {

        var session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).body(Map.of("loggedIn", false));
        }

        return ResponseEntity.ok(Map.of(
            "loggedIn", true,
            "userId", session.getAttribute("userId")
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        var session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok(Map.of("message", "Logout erfolgreich"));
    }
}

