package com.example.backend.models;

import jakarta.persistence.*;

@Entity
@Table(
    name = "userantwort",
    uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "question_id" })
)
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "choice_id", nullable = false)
    private Choice choice;

    public UserAnswer() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    public Choice getChoice() { return choice; }
    public void setChoice(Choice choice) { this.choice = choice; }
}
