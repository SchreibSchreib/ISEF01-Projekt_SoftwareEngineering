package com.example.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "passwort", nullable = false)
    private String passwort;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "userscore")
    private Integer userscore;

    // Konstruktoren
    public User() {}

    public User(String name, String passwort, Long teamId, Integer userscore) {
        this.name = name;
        this.passwort = passwort;
        this.teamId = teamId;
        this.userscore = userscore;
    }

    // Getter & Setter
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPasswort() { return passwort; }
    public void setPasswort(String passwort) { this.passwort = passwort; }

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public Integer getUserscore() { return userscore; }
    public void setUserscore(Integer userscore) { this.userscore = userscore; }
}

