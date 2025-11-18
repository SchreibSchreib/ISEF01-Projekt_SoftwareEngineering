package com.example.backend.controller;

import com.example.backend.models.User;
import com.example.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // ggf. auf dein Frontend einschränken
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
