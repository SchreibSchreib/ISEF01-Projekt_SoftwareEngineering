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

        // Creator direkt in dieses Team stecken
        creator.setTeam(saved);
        userRepository.save(creator);

        team.setJoinCode(generateJoinCode());

        return teamRepository.save(team);
    }

    private String generateJoinCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int idx = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(idx));
        }
        return sb.toString();
    }

    public Team joinTeamByCode(String code, long userId) {
        Team team = teamRepository.findByJoinCode(code);
        if (team == null) {
            throw new RuntimeException("Kein Team mit diesem Code gefunden: " + code);
        }
        return addUserToTeam(team.getTeamId(), userId);
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

    // Überprüfung, ob JoinCode gültig ist
    public boolean userHasValidTeam(User user) {
        Team team = user.getTeam();
        if (team == null)
            return false;

        return isValidJoinCode(team.getJoinCode());
    }

    // JoinCode Überprüfungslogik
    private boolean isValidJoinCode(String code) {
        if (code == null)
            return false;
        return code.matches("^[A-Z0-9]{6}$");
    }
}
