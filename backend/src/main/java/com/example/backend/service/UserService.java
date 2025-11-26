package com.example.backend.service;

import com.example.backend.models.Team;
import com.example.backend.models.User;
import com.example.backend.models.UserAnswer;
import com.example.backend.models.Question;
import com.example.backend.models.Choice;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.TeamRepository;
import com.example.backend.repository.QuestionRepository;
import com.example.backend.repository.ChoiceRepository;
import com.example.backend.repository.UserAnswerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;
    private final UserAnswerRepository userAnswerRepository;

    public UserService(UserRepository userRepository,
            TeamRepository teamRepository,
            QuestionRepository questionRepository,
            ChoiceRepository choiceRepository,
            UserAnswerRepository userAnswerRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.questionRepository = questionRepository;
        this.choiceRepository = choiceRepository;
        this.userAnswerRepository = userAnswerRepository;
    }

    /**
     * Login validieren
     */
    public boolean validateUser(String name, String passwort) {
        return userRepository.findByNameAndPasswort(name, passwort).isPresent();
    }

    /**
     * User per Name und Passwort holen
     */
    public User getUserByNameAndPassword(String name, String passwort) {
        return userRepository.findByNameAndPasswort(name, passwort)
                .orElse(null);
    }

    /**
     * User per ID holen
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User nicht gefunden: " + id));
    }

    /**
     * Alle User holen
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * UserScore erhöhen (bisherige Methode – z.B. noch genutzt vom alten Frontend)
     */
    public User addScore(long userId, int points) {
        User user = getUserById(userId);

        int currentScore = user.getUserscore() == null ? 0 : user.getUserscore();
        user.setUserscore(currentScore + points);

        return userRepository.save(user);
    }

    /**
     * User in Team stecken
     */
    public User setUserTeam(long userId, long teamId) {
        User user = getUserById(userId);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found: " + teamId));

        user.setTeam(team);
        return userRepository.save(user);
    }

    public User saveUser(User user) {
    return userRepository.save(user);
}

    /**
     * Speichert eine Antwort eines Users und vergibt ggf. Punkte.
     *
     * Regel:
     * - Pro Frage gibt es max. 10 Punkte, und zwar beim ersten Mal,
     * wenn der User diese Frage KORREKT beantwortet.
     * - Vorherige falsche Antworten blockieren die Punkte NICHT.
     */
    public int answerQuestion(long userId,
            long questionId,
            long choiceId,
            boolean correctFromFrontend) {

        // 1) Entities laden
        User user = getUserById(userId);

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Frage nicht gefunden: " + questionId));

        Choice choice = choiceRepository.findById(choiceId)
                .orElseThrow(() -> new RuntimeException("Auswahl nicht gefunden: " + choiceId));

        // Korrektheit zur Sicherheit auch im Backend bestimmen
        Long correctChoiceId = question.getCorrectChoice().getId(); // ggf. Feldnamen anpassen
        boolean isCorrect = choice.getId().equals(correctChoiceId);

        // 2) Prüfen, ob dieser User die Frage schon einmal beantwortet hat
        Optional<UserAnswer> existingOpt = userAnswerRepository.findByUserAndQuestion(user, question);

        if (existingOpt.isPresent()) {
            UserAnswer existing = existingOpt.get();
            boolean existingIsCorrect = existing.getChoice().getId().equals(correctChoiceId);

            // Fall A: Es gibt schon eine KORREKTE Antwort -> nie wieder Punkte
            if (existingIsCorrect) {
                // Antwort ggf. aktualisieren, aber keine Punkte mehr
                existing.setChoice(choice);
                userAnswerRepository.save(existing);
                return 0;
            }

            // Fall B: Es gibt eine falsche Antwort, neue ist ebenfalls falsch -> nix
            if (!isCorrect) {
                existing.setChoice(choice); // optional: letzte Antwort speichern
                userAnswerRepository.save(existing);
                return 0;
            }

            // Fall C: Es gibt eine falsche Antwort, neue ist JETZT korrekt
            existing.setChoice(choice);
            userAnswerRepository.save(existing);

            addPointsToUserAndTeam(user, 10);
            return 10;
        }

        // 3) Es gibt noch keinen Eintrag für (User, Frage)
        UserAnswer answer = new UserAnswer();
        answer.setUser(user);
        answer.setQuestion(question);
        answer.setChoice(choice);
        userAnswerRepository.save(answer);

        if (isCorrect) {
            addPointsToUserAndTeam(user, 10);
            return 10;
        }

        return 0;
    }

    /**
     * Hilfsmethode: Punkte vergeben an User (+ Team, falls vorhanden)
     */
    private void addPointsToUserAndTeam(User user, int points) {
        int currentUserScore = user.getUserscore() == null ? 0 : user.getUserscore();
        user.setUserscore(currentUserScore + points);
        userRepository.save(user);

        if (user.getTeam() != null) {
            Team team = user.getTeam();
            int currentTeamScore = team.getTeamscore() == null ? 0 : team.getTeamscore();
            team.setTeamscore(currentTeamScore + points);
            teamRepository.save(team);
        }
    }
}
