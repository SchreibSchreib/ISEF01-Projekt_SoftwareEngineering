package com.example.backend.controller;

import com.example.backend.dto.LoginRequest;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest http) {
        boolean valid = userService.validateUser(request.getName(), request.getPasswort());

        if (valid) {
            http.getSession(true).setAttribute("user", request.getName());

            return ResponseEntity.ok(Map.of("message", "Login erfolgreich"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Ungültige Anmeldedaten"));
        }
    }
}
