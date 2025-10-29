package com.example.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5173") // erlaubt Frontend-Anfragen
@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String hello() {
        return "Lügner!";
    }
}