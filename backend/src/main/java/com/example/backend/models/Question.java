package com.example.backend.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "frage")  // DB-Tabelle bleibt deutsch!
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "frage_id")
    private Long id;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    // richtigeAntwort_id → auswahlmoeglichkeit.auswahl_id
    @ManyToOne
    @JoinColumn(name = "richtigeAntwort_id")
    private Choice correctChoice;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Choice> choices;

    public Question() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Choice getCorrectChoice() { return correctChoice; }
    public void setCorrectChoice(Choice correctChoice) { this.correctChoice = correctChoice; }

    public List<Choice> getChoices() { return choices; }
    public void setChoices(List<Choice> choices) { this.choices = choices; }
}
