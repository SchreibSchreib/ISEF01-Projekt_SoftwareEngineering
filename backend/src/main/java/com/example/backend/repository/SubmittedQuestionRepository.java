package com.example.backend.repository;

import com.example.backend.models.SubmittedQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmittedQuestionRepository extends JpaRepository<SubmittedQuestion, Long> {

    List<SubmittedQuestion> findByApproved(Integer approved);
}
