package com.example.backend.controller;

import com.example.backend.models.SubmittedQuestion;
import com.example.backend.models.User;
import com.example.backend.service.SubmittedQuestionService;
import com.example.backend.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submitted-questions")
public class SubmittedQuestionController {

    private final SubmittedQuestionService service;
    private final UserService userService;

    public SubmittedQuestionController(
            SubmittedQuestionService service,
            UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping
    public SubmittedQuestion submit(
            @RequestBody SubmittedQuestion q,
            HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");

        if (userId == null) {
            throw new RuntimeException("Nicht eingeloggt");
        }

        User user = userService.getUserById(userId);
        q.setSubmittedBy(user);

        return service.submit(q);
    }

    @GetMapping("/pending")
    public List<SubmittedQuestion> pending(HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("Nicht eingeloggt");
        }

        return service.getPendingForUser(userId);
    }

    @PostMapping("/{id}/vote")
    public SubmittedQuestion vote(
            @PathVariable Long id,
            @RequestParam boolean positive,
            HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("Nicht eingeloggt");
        }

        return service.vote(id, positive, userId);
    }
}
