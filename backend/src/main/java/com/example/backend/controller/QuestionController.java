package com.example.backend.controller;

import com.example.backend.models.Question;
import com.example.backend.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*") 
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
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
}
