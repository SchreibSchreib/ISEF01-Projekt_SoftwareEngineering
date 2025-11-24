package com.example.backend.models;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "team")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "name", nullable = false)
    private String name;

    // User, der das Team erstellt hat (FK created_by)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private User createdBy;

    @Column(name = "teamscore", nullable = false)
    private Integer teamscore = 0;

    // Liste aller Benutzer, die diesem Team angehören
    @OneToMany(mappedBy = "team")
    @JsonIgnoreProperties({ "team" })
    private List<User> users;

    public Team() {
    }

    public Team(String name, User createdBy, Integer teamscore) {
        this.name = name;
        this.createdBy = createdBy;
        this.teamscore = teamscore;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getTeamscore() {
        return teamscore;
    }

    public void setTeamscore(Integer teamscore) {
        this.teamscore = teamscore;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
