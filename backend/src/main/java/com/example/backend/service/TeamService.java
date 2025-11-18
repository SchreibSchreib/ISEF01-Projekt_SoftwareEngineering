package com.example.backend.service;

import com.example.backend.models.Team;
import com.example.backend.models.User;
import com.example.backend.repository.TeamRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamService(TeamRepository teamRepository,
                       UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    // Alle Teams
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    // Ein Team nach ID
    public Team getTeamById(long teamId) {   
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team nicht gefunden: " + teamId));
    }

    // Neues Team anlegen (mit Creator)
    public Team createTeam(String name, long creatorUserId) {  
        User creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new RuntimeException("User nicht gefunden: " + creatorUserId));

        Team team = new Team();
        team.setName(name);
        team.setCreatedBy(creator);
        team.setTeamscore(0);

        Team saved = teamRepository.save(team);

        // Optional: Creator direkt in dieses Team stecken
        creator.setTeam(saved);
        userRepository.save(creator);

        return saved;
    }

    // User tritt einem Team bei
    public Team addUserToTeam(long teamId, long userId) { 
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team nicht gefunden: " + teamId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User nicht gefunden: " + userId));

        user.setTeam(team);
        userRepository.save(user);

        return team;
    }

    // Team-Score erhöhen (z.B. wenn jemand eine Frage richtig beantwortet)
    public Team addScoreToTeam(long teamId, int points) {  
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team nicht gefunden: " + teamId));

        team.setTeamscore(team.getTeamscore() + points);
        return teamRepository.save(team);
    }
}
