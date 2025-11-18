package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "auswahlmoeglichkeit")  // DB-Name bleibt deutsch!
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auswahl_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "frage_id", nullable = false)
    @JsonIgnore
    private Question question;

    @Enumerated(EnumType.STRING)
    @Column(name = "label", nullable = false)
    private Label label;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    public enum Label {
        A, B, C, D
    }

    public Choice() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    public Label getLabel() { return label; }
    public void setLabel(Label label) { this.label = label; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
