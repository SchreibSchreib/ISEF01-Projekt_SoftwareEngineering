package com.example.backend.service;

import com.example.backend.models.*;
import com.example.backend.repository.ChoiceRepository;
import com.example.backend.repository.QuestionRepository;
import com.example.backend.repository.SubmittedQuestionRepository;
import com.example.backend.repository.SubmittedQuestionVoteRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubmittedQuestionService {

    private final SubmittedQuestionRepository submittedRepo;
    private final QuestionRepository questionRepo;
    private final SubmittedQuestionVoteRepository voteRepo;
    private final UserService userService;
    private final ChoiceRepository choiceRepo;

    public SubmittedQuestionService(
            SubmittedQuestionRepository submittedRepo,
            QuestionRepository questionRepo,
            SubmittedQuestionVoteRepository voteRepo,
            UserService userService,
            ChoiceRepository choiceRepo) {
        this.submittedRepo = submittedRepo;
        this.questionRepo = questionRepo;
        this.voteRepo = voteRepo;
        this.userService = userService;
        this.choiceRepo = choiceRepo;
    }

    public SubmittedQuestion submit(SubmittedQuestion q) {
        q.setApproved(0);
        q.setPositiveVotes(0);
        q.setNegativeVotes(0);
        return submittedRepo.save(q);
    }

    // neutrale Methode ohne Filter
    public List<SubmittedQuestion> getPending() {
        return submittedRepo.findByApproved(0);
    }

    // filtert raus, was der User bereits bewertet hat
    public List<SubmittedQuestion> getPendingForUser(Long userId) {
        User user = userService.getUserById(userId);

        // alle offenen Fragen holen (approved = 0)
        List<SubmittedQuestion> pending = submittedRepo.findByApproved(0);

        return pending.stream()
                .filter(q -> q.getSubmittedBy() == null || !q.getSubmittedBy().getUserId().equals(userId))
                .filter(q -> voteRepo.findByUserAndQuestion(user, q).isEmpty())
                .toList();
    }

    public SubmittedQuestion vote(Long id, boolean positive, Long userId) {

        SubmittedQuestion sq = submittedRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        User user = userService.getUserById(userId);

        // User darf eigene Frage nicht bewerten
        if (sq.getSubmittedBy() != null &&
                sq.getSubmittedBy().getUserId().equals(userId)) {
            throw new RuntimeException("Eigene Frage kann nicht bewertet werden.");
        }

        // User darf Frage nur einmal bewerten
        Optional<SubmittedQuestionVote> existingVoteOpt = voteRepo.findByUserAndQuestion(user, sq);

        if (existingVoteOpt.isPresent()) {
            throw new RuntimeException("Du hast diese Frage bereits bewertet.");
        }

        // Vote speichern
        SubmittedQuestionVote vote = new SubmittedQuestionVote();
        vote.setQuestion(sq);
        vote.setUser(user);
        vote.setPositive(positive);
        voteRepo.save(vote);

        if (positive)
            sq.setPositiveVotes(sq.getPositiveVotes() + 1);
        else
            sq.setNegativeVotes(sq.getNegativeVotes() + 1);

        if (sq.getApproved() == null)
            sq.setApproved(0);

        if (sq.getApproved() == 0) {

            if (sq.getPositiveVotes() >= 3) {
                acceptQuestion(sq);
                sq.setApproved(1);
            }

            else if (sq.getNegativeVotes() >= 3) {
                sq.setApproved(-1);
            }
        }

        return submittedRepo.save(sq);
    }

    // Frage in echten Fragenpool übernehmen
    private void acceptQuestion(SubmittedQuestion sq) {

        // Neue Question speichern (für ID)
        Question q = new Question();
        q.setText(sq.getText());
        q.setCorrectAnswerExplanation(sq.getExplanation());
        q = questionRepo.save(q);

        // Choices erzeugen UND sofort speichern
        Choice a = new Choice();
        a.setLabel(Choice.Label.A);
        a.setText(sq.getChoiceA());
        a.setQuestion(q);
        a = choiceRepo.save(a);

        Choice b = new Choice();
        b.setLabel(Choice.Label.B);
        b.setText(sq.getChoiceB());
        b.setQuestion(q);
        b = choiceRepo.save(b);

        Choice c = new Choice();
        c.setLabel(Choice.Label.C);
        c.setText(sq.getChoiceC());
        c.setQuestion(q);
        c = choiceRepo.save(c);

        Choice d = new Choice();
        d.setLabel(Choice.Label.D);
        d.setText(sq.getChoiceD());
        d.setQuestion(q);
        d = choiceRepo.save(d);

        // richtige Antwort bestimmen
        Choice correct = switch (sq.getCorrectAnswer()) {
            case "A" -> a;
            case "B" -> b;
            case "C" -> c;
            case "D" -> d;
            default -> throw new RuntimeException("Invalid correct answer");
        };

        q.setCorrectChoice(correct);

        // Frage final speichern → correctChoice wird gesetzt
        questionRepo.save(q);
    }
}
