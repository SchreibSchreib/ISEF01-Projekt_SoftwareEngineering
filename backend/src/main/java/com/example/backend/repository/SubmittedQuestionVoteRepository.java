package com.example.backend.repository;

import com.example.backend.models.SubmittedQuestion;
import com.example.backend.models.SubmittedQuestionVote;
import com.example.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmittedQuestionVoteRepository extends JpaRepository<SubmittedQuestionVote, Long> {

    Optional<SubmittedQuestionVote> findByUserAndQuestion(User user, SubmittedQuestion question);
}
