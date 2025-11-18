package com.example.backend.repository;

import com.example.backend.models.UserAnswer;
import com.example.backend.models.User;
import com.example.backend.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

    // Prüfen, ob ein User eine bestimmte Frage schon beantwortet hat
    Optional<UserAnswer> findByUserAndQuestion(User user, Question question);
}
