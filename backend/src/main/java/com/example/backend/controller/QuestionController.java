package com.example.backend.controller;

import com.example.backend.dto.CreateQuestionRequest;
import com.example.backend.models.Question;
import com.example.backend.service.QuestionCreateService;
import com.example.backend.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionCreateService questionCreateService;

    public QuestionController(QuestionService questionService,
            QuestionCreateService questionCreateService) {
        this.questionService = questionService;
        this.questionCreateService = questionCreateService;
    }

    // Alle Fragen holen
    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    // Einzelne Frage nach ID holen
    @GetMapping("/{id}")
    public Question getQuestionById(@PathVariable long id) {
        return questionService.getQuestionById(id);
    }

    // Zufällige Fragen holen
    @GetMapping("/random/{count}")
    public List<Question> getRandomQuestions(@PathVariable int count) {
        return questionService.getRandomQuestions(count);
    }

    // Neue Frage anlegen
    @PostMapping
    public Question createQuestion(@RequestBody CreateQuestionRequest request) {
        return questionCreateService.createQuestion(request);
    }
}
