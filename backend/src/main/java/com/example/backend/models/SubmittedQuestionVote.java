package com.example.backend.models;

import jakarta.persistence.*;

@Entity
public class SubmittedQuestionVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private SubmittedQuestion question;

    @ManyToOne(optional = false)
    private User user;

    private boolean positive;

    // Getter & Setter
    public Long getId() { return id; }

    public SubmittedQuestion getQuestion() { return question; }
    public void setQuestion(SubmittedQuestion question) { this.question = question; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public boolean isPositive() { return positive; }
    public void setPositive(boolean positive) { this.positive = positive; }
}
