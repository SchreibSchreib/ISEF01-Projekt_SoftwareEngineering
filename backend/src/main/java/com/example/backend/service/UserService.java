package com.example.backend.service;

import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean validateUser(String name, String passwort) {
        return userRepository.findByUsernameAndPassword(name, passwort).isPresent();
    }
}

