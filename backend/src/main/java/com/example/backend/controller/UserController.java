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

    public static class AnswerRequest {
        public long questionId;
        public long choiceId;
        public boolean correct;

        public AnswerRequest() {
        }
    }

    public static class AnswerResponse {
        public boolean correct;
        public int gainedPoints;
        public int totalScore;

        public AnswerResponse(boolean correct, int gainedPoints, int totalScore) {
            this.correct = correct;
            this.gainedPoints = gainedPoints;
            this.totalScore = totalScore;
        }
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

    @PostMapping("/{userId}/answer")
    public ResponseEntity<AnswerResponse> answerQuestion(
            @PathVariable long userId,
            @RequestBody AnswerRequest request
    ) {
        // ruft deine neue Logik im UserService auf
        int gained = userService.answerQuestion(
                userId,
                request.questionId,
                request.choiceId,
                request.correct
        );

        User user = userService.getUserById(userId);

        AnswerResponse resp = new AnswerResponse(
                request.correct,
                gained,
                user.getUserscore()
        );

        return ResponseEntity.ok(resp);
    }
}
