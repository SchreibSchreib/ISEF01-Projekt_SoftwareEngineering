package com.example.backend.service;

import com.example.backend.dto.TeamMessageDto;
import com.example.backend.models.Team;
import com.example.backend.models.TeamMessage;
import com.example.backend.models.User;
import com.example.backend.repository.TeamMessageRepository;
import com.example.backend.repository.TeamRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamChatService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMessageRepository messageRepository;

    public TeamChatService(TeamRepository teamRepository,
                           UserRepository userRepository,
                           TeamMessageRepository messageRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public List<TeamMessageDto> getMessagesForTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        return messageRepository.findByTeamOrderByCreatedAtAsc(team)
                .stream()
                .map(m -> new TeamMessageDto(
                        m.getId(),
                        m.getUser().getUserId(),
                        m.getUser().getName(),
                        m.getContent(),
                        m.getCreatedAt()
                ))
                .toList();
    }

    public TeamMessageDto addMessage(Long teamId, Long userId, String content) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        TeamMessage msg = new TeamMessage();
        msg.setTeam(team);
        msg.setUser(user);
        msg.setContent(content);

        TeamMessage saved = messageRepository.save(msg);

        return new TeamMessageDto(
                saved.getId(),
                user.getUserId(),
                user.getName(),
                saved.getContent(),
                saved.getCreatedAt()
        );
    }
}
