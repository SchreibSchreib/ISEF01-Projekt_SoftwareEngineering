package com.example.backend.controller;

import com.example.backend.dto.TeamMessageDto;
import com.example.backend.models.Team;
import com.example.backend.service.TeamService;
import com.example.backend.service.TeamChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;
    private final TeamChatService teamChatService;

    public TeamController(TeamService teamService,
                          TeamChatService teamChatService) {
        this.teamService = teamService;
        this.teamChatService = teamChatService;
    }

    // --------------------------------------------------------
    //  Alle Teams laden
    // --------------------------------------------------------
    @GetMapping
    public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }

    // --------------------------------------------------------
    //  Team nach ID
    // --------------------------------------------------------
    @GetMapping("/{id}")
    public Team getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    // --------------------------------------------------------
    //  Team erstellen
    // --------------------------------------------------------
    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        Number creatorIdNum = (Number) body.get("creatorUserId");

        if (name == null || creatorIdNum == null) {
            return ResponseEntity.badRequest().build();
        }

        Long creatorUserId = creatorIdNum.longValue();
        Team team = teamService.createTeam(name, creatorUserId);

        return ResponseEntity.ok(team);
    }

    // --------------------------------------------------------
    //  Team beitreten per Join-Code
    // --------------------------------------------------------
    @PostMapping("/joinByCode")
    public ResponseEntity<Team> joinByCode(
            @RequestParam String code,
            @RequestParam Long userId
    ) {
        Team team = teamService.joinTeamByCode(code, userId);
        return ResponseEntity.ok(team);
    }

    // --------------------------------------------------------
    //  User tritt Team per teamId bei (alter Endpunkt)
    // --------------------------------------------------------
    @PostMapping("/{teamId}/join")
    public ResponseEntity<Team> joinTeam(
            @PathVariable Long teamId,
            @RequestParam Long userId
    ) {
        Team team = teamService.addUserToTeam(teamId, userId);
        return ResponseEntity.ok(team);
    }

    // --------------------------------------------------------
    //  Teamscore erhöhen
    // --------------------------------------------------------
    @PostMapping("/{teamId}/addScore")
    public ResponseEntity<Team> addScore(
            @PathVariable Long teamId,
            @RequestParam int points
    ) {
        Team team = teamService.addScoreToTeam(teamId, points);
        return ResponseEntity.ok(team);
    }

    // --------------------------------------------------------
    //  Alle Team-Chat Nachrichten laden
    // --------------------------------------------------------
    @GetMapping("/{teamId}/messages")
    public List<TeamMessageDto> getTeamMessages(@PathVariable Long teamId) {
        return teamChatService.getMessagesForTeam(teamId);
    }

    // --------------------------------------------------------
    //  Neue Chat-Nachricht senden
    // --------------------------------------------------------
    @PostMapping("/{teamId}/messages")
    public ResponseEntity<TeamMessageDto> sendMessage(
            @PathVariable Long teamId,
            @RequestBody Map<String, Object> body
    ) {
        String content = (String) body.get("content");
        Number userIdNum = (Number) body.get("userId");

        if (content == null || content.isBlank() || userIdNum == null) {
            return ResponseEntity.badRequest().build();
        }

        Long userId = userIdNum.longValue();
        TeamMessageDto dto = teamChatService.addMessage(teamId, userId, content.trim());

        return ResponseEntity.ok(dto);
    }
}
