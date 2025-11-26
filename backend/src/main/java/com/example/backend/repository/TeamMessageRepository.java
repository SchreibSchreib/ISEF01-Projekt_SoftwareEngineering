package com.example.backend.repository;

import com.example.backend.models.Team;
import com.example.backend.models.TeamMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMessageRepository extends JpaRepository<TeamMessage, Long> {

    List<TeamMessage> findByTeamOrderByCreatedAtAsc(Team team);
}
