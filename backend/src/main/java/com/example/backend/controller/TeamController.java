package com.example.backend.controller;

import com.example.backend.models.Team;
import com.example.backend.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    // Alle Teams
    @GetMapping
    public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }

    // Team nach ID
    @GetMapping("/{id}")
    public Team getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    // Team erstellen
    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        Number creatorIdNum = (Number) body.get("creatorUserId");
        Long creatorUserId = creatorIdNum != null ? creatorIdNum.longValue() : null;

        if (name == null || creatorUserId == null) {
            return ResponseEntity.badRequest().build();
        }

        Team team = teamService.createTeam(name, creatorUserId);
        return ResponseEntity.ok(team);
    }

    // User tritt einem Team bei
    @PostMapping("/{teamId}/join")
    public ResponseEntity<Team> joinTeam(@PathVariable Long teamId,
                                         @RequestParam Long userId) {
        Team team = teamService.addUserToTeam(teamId, userId);
        return ResponseEntity.ok(team);
    }

    // Teamscore erhöhen
    @PostMapping("/{teamId}/addScore")
    public ResponseEntity<Team> addScore(@PathVariable Long teamId,
                                         @RequestParam int points) {
        Team team = teamService.addScoreToTeam(teamId, points);
        return ResponseEntity.ok(team);
    }
}
