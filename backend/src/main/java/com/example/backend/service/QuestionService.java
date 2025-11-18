package com.example.backend.service;

import com.example.backend.models.Question;
import com.example.backend.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Alle Fragen inkl. Choices laden.
     */
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    /**
     * Einzelne Frage per ID laden.
     */
    public Question getQuestionById(long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found: " + questionId));
    }
}
