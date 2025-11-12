package com.example.backend.dto;

public class LoginRequest {
    private String name;
    private String passwort;

    public String getName() {
        return name;
    }

    public void setName(String username) {
        this.name = username;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }
}