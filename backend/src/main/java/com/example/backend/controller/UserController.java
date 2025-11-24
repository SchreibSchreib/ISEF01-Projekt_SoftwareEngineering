package com.example.backend.controller;

import com.example.backend.models.User;
import com.example.backend.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Alle User holen
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // User per ID holen
    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInUser(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body("Keine gültige Session");
        }

        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // UserScore erhöhen
    @PostMapping("/{id}/addScore")
    public ResponseEntity<User> addScore(@PathVariable long id,
            @RequestParam int points) {
        User updated = userService.addScore(id, points);
        return ResponseEntity.ok(updated);
    }

    // User einem Team zuordnen
    @PostMapping("/{userId}/team/{teamId}")
    public ResponseEntity<User> setUserTeam(@PathVariable long userId,
            @PathVariable long teamId) {
        User updated = userService.setUserTeam(userId, teamId);
        return ResponseEntity.ok(updated);
    }
}
