package com.example.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "passwort", nullable = false)
    private String passwort;

    // ⚠ NEU: statt Long teamId -> Beziehung zu Team
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")   // benutzt die Spalte team_id in der Tabelle
    private Team team;

    @Column(name = "userscore", nullable = false)
    private Integer userscore = 0;  // Standardwert

    // Konstruktoren
    public User() {}

    public User(String name, String passwort, Team team, Integer userscore) {
        this.name = name;
        this.passwort = passwort;
        this.team = team;
        this.userscore = userscore;
    }

    // Getter & Setter
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPasswort() { return passwort; }
    public void setPasswort(String passwort) { this.passwort = passwort; }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }

    public Integer getUserscore() { return userscore; }
    public void setUserscore(Integer userscore) { this.userscore = userscore; }
}
