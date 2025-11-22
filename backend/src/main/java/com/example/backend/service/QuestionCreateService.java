package com.example.backend.service;

import com.example.backend.dto.CreateQuestionRequest;
import com.example.backend.models.Choice;
import com.example.backend.models.Question;
import com.example.backend.repository.ChoiceRepository;
import com.example.backend.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionCreateService {

    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;

    public QuestionCreateService(QuestionRepository questionRepository, ChoiceRepository choiceRepository) {
        this.questionRepository = questionRepository;
        this.choiceRepository = choiceRepository;
    }

    @Transactional
    public Question createQuestion(CreateQuestionRequest req) {

        validateRequest(req);

        // Frage speichern
        Question question = new Question();
        question.setText(req.getText());

        // Erklärung hinzufügen
        question.setCorrectAnswerExplanation(req.getExplanation());

        question = questionRepository.save(question);

        // Antwortmöglichkeiten erstellen
        List<Choice> choices = new ArrayList<>();

        for (String label : List.of("A", "B", "C", "D")) {
            Choice choice = new Choice();
            choice.setQuestion(question);
            choice.setLabel(Choice.Label.valueOf(label));
            choice.setText(req.getChoices().get(label));
            choices.add(choice);
        }

        // speichern
        choices = choiceRepository.saveAll(choices);

        // richtige Antwort finden
        Choice correct = choices.stream()
                .filter(c -> c.getLabel().name().equals(req.getCorrect()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid correct label"));

        // Frage aktualisieren
        question.setCorrectChoice(correct);
        question.setChoices(choices);

        return questionRepository.save(question);
    }

    private void validateRequest(CreateQuestionRequest req) {

        if (req.getText() == null || req.getText().isBlank()) {
            throw new IllegalArgumentException("Fragetext darf nicht leer sein.");
        }

        if (req.getExplanation() == null || req.getExplanation().isBlank()) {
            throw new IllegalArgumentException("Begründung darf nicht leer sein.");
        }

        if (req.getChoices() == null) {
            throw new IllegalArgumentException("Antwortmöglichkeiten fehlen.");
        }

        // Prüfe, ob alle Labels existieren
        for (String label : List.of("A", "B", "C", "D")) {

            if (!req.getChoices().containsKey(label)) {
                throw new IllegalArgumentException("Antwort " + label + " fehlt.");
            }

            String text = req.getChoices().get(label);

            if (text == null || text.isBlank()) {
                throw new IllegalArgumentException("Antwort " + label + " darf nicht leer sein.");
            }
        }

        // korrekte Antwort prüfen
        if (req.getCorrect() == null || !List.of("A", "B", "C", "D").contains(req.getCorrect())) {
            throw new IllegalArgumentException("Korrekte Antwort muss A, B, C oder D sein.");
        }
    }
}
