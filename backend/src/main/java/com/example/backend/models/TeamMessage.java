package com.example.backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "team_message")
public class TeamMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 passt zu team_message.team_id
    @ManyToOne(optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    // 🔹 passt zu team_message.user_id
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 500)
    private String content;

    // 🔹 passt zu team_message.created_at
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // ----- Getter / Setter -----

    public Long getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
