package com.example.backend.service;

import com.example.backend.models.Team;
import com.example.backend.models.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public UserService(UserRepository userRepository,
                       TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    /**
     * Login validieren
     */
    public boolean validateUser(String name, String passwort) {
        return userRepository.findByNameAndPasswort(name, passwort).isPresent();
    }

    /**
     * User per ID holen
     */
    public User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
    }

    /**
     * Alle User holen
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * UserScore erhöhen (Quiz richtig beantwortet)
     */
    public User addScore(long userId, int points) {
        User user = getUserById(userId);

        int currentScore = user.getUserscore() == null ? 0 : user.getUserscore();
        user.setUserscore(currentScore + points);

        return userRepository.save(user);
    }

    /**
     * User in Team stecken
     */
    public User setUserTeam(long userId, long teamId) {
        User user = getUserById(userId);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found: " + teamId));

        user.setTeam(team);
        return userRepository.save(user);
    }
}
