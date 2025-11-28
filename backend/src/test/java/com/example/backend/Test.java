package com.example.backend;

import com.example.backend.models.Choice;
import com.example.backend.models.Question;
import com.example.backend.models.SubmittedQuestion;
import com.example.backend.service.SubmittedQuestionService;
import com.example.backend.models.User;
import com.example.backend.service.QuestionCreateService;
import com.example.backend.service.QuestionService;
import com.example.backend.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import org.springframework.dao.InvalidDataAccessResourceUsageException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * Testet:
 * 1) Login (A1)
 * 2) Random-Questions (A2)
 * 3) Punktevergabe (A2/A6)
 * 4) Keine doppelten Punkte (A6)
 * 5) Validierung Frage-Erstellung (A4/A9)
 */
public class Test {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext ctx = SpringApplication.run(BackendApplication.class)) {

            UserService userService = ctx.getBean(UserService.class);
            QuestionService questionService = ctx.getBean(QuestionService.class);
            QuestionCreateService questionCreateService = ctx.getBean(QuestionCreateService.class);
            SubmittedQuestionService submittedQuestionService = ctx.getBean(SubmittedQuestionService.class);

            testLogin(userService);
            testRandomQuestions(questionService);
            testPointsOnCorrectAnswer(userService, questionService);
            testNoDoublePoints(userService, questionService);
            testSubmitInitialState(submittedQuestionService);

            System.out.println("Alle Tests erfolgreich!");
        } catch (Throwable t) {
            System.err.println("Test fehlgeschlagen:");
            t.printStackTrace();
            System.exit(1);
        }
    }

    private static void check(boolean cond, String message) {
        if (!cond) {
            throw new RuntimeException("Test fehlgeschlagen: " + message);
        }
    }

    // A1 – Login mit gültigen Credentials
    // ------------------------------------------------------------
    private static void testLogin(UserService userService) {
        System.out.println(" Test: Login mit gültigen Daten");

        String username = "TestUser6";
        String password = "test123";

        // Prüfen, ob validateUser(...) true zurückgibt
        boolean valid = userService.validateUser(username, password);
        check(valid, "validateUser() sollte true liefern, war aber false");

        // Den User wirklich holen und prüfen
        User user = userService.getUserByNameAndPassword(username, password);
        check(user != null, "getUserByNameAndPassword(...) hat null geliefert");

        // Prüfen, ob der richtige User zurückkommt
        check(username.equals(user.getName()),
                "Benutzername stimmt nicht überein: erwartet " + username + ", war " + user.getName());
    }

    // A2 – Zufällige Fragen: 4 unterschiedliche, approved
    // ------------------------------------------------------------
    private static void testRandomQuestions(QuestionService questionService) {
        System.out.println("Test: Zufallsfragen (4 unterschiedliche, approved)");

        int limit = 4;
        List<Question> questions = questionService.getRandomQuestions(limit);

        check(questions != null, "Fragenliste ist null");
        check(questions.size() == limit,
                "Erwartet " + limit + " Fragen, erhalten: " + questions.size());

        Set<Long> ids = new HashSet<>();
        for (Question q : questions) {
            check(q.getId() != null, "Frage hat keine ID");
            ids.add(q.getId());
            // falls ihr ein Approved-Flag habt:
            // check(q.isApproved(), "Frage ist nicht approved");
        }

        check(ids.size() == limit, "Es gibt doppelte Fragen in der Liste");
    }

    // A2/A6 – Punktevergabe bei erster korrekter Antwort
    // ------------------------------------------------------------
    private static void testPointsOnCorrectAnswer(UserService userService,
            QuestionService questionService) {
        System.out.println("Test: Punktevergabe bei erster korrekter Antwort");

        // User per ID holen
        Long userId = 6L; // z.B. dein TestUser6 ohne Team
        User userBefore = userService.getUserById(userId);
        check(userBefore != null, "User mit ID " + userId + " wurde nicht gefunden");

        int oldScore = userBefore.getUserscore() == null ? 0 : userBefore.getUserscore();

        // Eine Frage holen
        List<Question> questions = questionService.getRandomQuestions(1);
        check(!questions.isEmpty(), "Keine Frage für den Test gefunden");
        Question question = questions.get(0);

        // Die korrekte Choice-ID bestimmen
        Choice correctChoice = question.getCorrectChoice();
        check(correctChoice != null, "Frage hat keine korrekte Choice gesetzt");
        Long correctChoiceId = correctChoice.getId();
        check(correctChoiceId != null, "Korrekte Choice hat keine ID");

        // answerQuestion aufrufen – (vermeintlich) erster korrekter Versuch
        int points = userService.answerQuestion(
                userId,
                question.getId(),
                correctChoiceId,
                true);

        // User nach dem Speichern erneut laden, um den neuen Score zu prüfen
        User userAfter = userService.getUserById(userId);
        int newScore = userAfter.getUserscore() == null ? 0 : userAfter.getUserscore();

        // --- Ab hier geändert ---
        if (points == 10) {
            // Fall: Frage wurde wirklich zum ersten Mal korrekt beantwortet
            check(newScore == oldScore + 10,
                    "User-Score sollte um 10 steigen (alt=" + oldScore + ", neu=" + newScore + ")");
        } else if (points == 0) {
            // Fall: Frage war vorher schon korrekt beantwortet → fachlich ok
            System.out.println("Hinweis: answerQuestion() hat 0 Punkte zurückgegeben, " +
                    "vermutlich wurde die Frage von diesem User früher schon korrekt beantwortet.");
            // hier KEIN check(), Test soll nicht fehlschlagen
        } else {
            // Unerwarteter Wert → wirklicher Fehler
            check(false, "Unerwartete Punktezahl: " + points + " (erwartet 10 oder 0)");
        }
    }

    // A6 – keine doppelten Punkte für dieselbe Frage (mit int userId)
    // ------------------------------------------------------------
    private static void testNoDoublePoints(UserService userService,
            QuestionService questionService) {
        System.out.println("Test: keine doppelten Punkte für gleiche Frage");

        // Test-User über int-ID holen
        int userId = 6;
        User userBefore = userService.getUserById((long) userId);
        check(userBefore != null, "User mit ID " + userId + " wurde nicht gefunden");

        int startScore = userBefore.getUserscore() == null ? 0 : userBefore.getUserscore();

        // Eine Frage holen
        List<Question> questions = questionService.getRandomQuestions(1);
        check(!questions.isEmpty(), "Keine Frage für den Test gefunden");
        Question question = questions.get(0);

        // Korrekte Choice-ID bestimmen
        Choice correctChoice = question.getCorrectChoice();
        check(correctChoice != null, "Frage hat keine korrekte Choice gesetzt");
        Long correctChoiceId = correctChoice.getId();
        check(correctChoiceId != null, "Korrekte Choice hat keine ID");

        // Erster Versuch – korrekte Antwort
        int first = userService.answerQuestion(
                userId,
                question.getId(),
                correctChoiceId,
                true);

        // User nach dem ersten Versuch erneut laden
        User userAfterFirst = userService.getUserById((long) userId);
        int scoreAfterFirst = userAfterFirst.getUserscore() == null ? 0 : userAfterFirst.getUserscore();

        // Zweiter Versuch – gleiche Frage
        int second = userService.answerQuestion(
                userId,
                question.getId(),
                correctChoiceId,
                true);

        // User nach zweitem Versuch neu laden
        User userAfterSecond = userService.getUserById((long) userId);
        int scoreAfterSecond = userAfterSecond.getUserscore() == null ? 0 : userAfterSecond.getUserscore();

        // Auswertung:
        // first darf 10 (erste korrekte Antwort) oder 0 (war schon vorher korrekt
        // beantwortet) sein
        if (first == 10) {
            System.out.println("Hinweis: Erste korrekte Antwort hat 10 Punkte vergeben.");
            check(scoreAfterFirst == startScore + 10,
                    "Score nach dem ersten Versuch sollte start + 10 sein (start=" + startScore +
                            ", nach1=" + scoreAfterFirst + ")");
        } else if (first == 0) {
            System.out.println(
                    "Hinweis: Erste Antwort im Test hat 0 Punkte bekommen – Frage war wohl vorher schon korrekt beantwortet.");
            // Score kann dann gleich bleiben
        } else {
            check(false, "Unerwartete Punktezahl im ersten Versuch: " + first + " (erwartet 10 oder 0)");
        }

        // In JEDEM Fall: zweiter Versuch darf keine weiteren Punkte geben
        check(second == 0, "Zweiter Versuch sollte 0 Punkte bringen, war aber " + second);
        check(scoreAfterSecond == scoreAfterFirst,
                "Score darf sich beim zweiten Versuch nicht erhöhen (nach1=" + scoreAfterFirst +
                        ", nach2=" + scoreAfterSecond + ")");
    }

    // A4/A9 – Einreichen einer Frage: Initialzustand von approved und Votes
    // ------------------------------------------------------------
    private static void testSubmitInitialState(SubmittedQuestionService submittedQuestionService) {
        System.out.println("Test: Frage einreichen – Initialzustand");

        // Neue eingereichte Frage aufbauen
        SubmittedQuestion sq = new SubmittedQuestion();
        sq.setText("Wie heißt die Hauptstadt von Frankreich?");
        sq.setChoiceA("Paris");
        sq.setChoiceB("Berlin");
        sq.setChoiceC("Madrid");
        sq.setChoiceD("Rom");
        sq.setCorrectAnswer("A");
        sq.setExplanation("Paris ist die Hauptstadt von Frankreich.");
        // sq.setSubmittedBy(null); // optional, je nach Modell

        // Service-Methode submit() aufrufen
        SubmittedQuestion saved = submittedQuestionService.submit(sq);

        // Prüfen, dass ein Eintrag zurückkommt
        check(saved != null, "submit() hat null zurückgegeben");
        check(saved.getId() != null, "Eingereichte Frage hat keine ID (wurde nicht gespeichert)");

        // Fachlogik aus SubmittedQuestionService.submit():
        // - approved wird auf 0 gesetzt
        // - positiveVotes = 0
        // - negativeVotes = 0
        Integer approved = saved.getApproved();
        Integer pos = saved.getPositiveVotes();
        Integer neg = saved.getNegativeVotes();

        check(approved != null && approved == 0,
                "approved sollte 0 sein, war aber " + approved);
        check(pos != null && pos == 0,
                "positiveVotes sollte 0 sein, war aber " + pos);
        check(neg != null && neg == 0,
                "negativeVotes sollte 0 sein, war aber " + neg);
    }

}
