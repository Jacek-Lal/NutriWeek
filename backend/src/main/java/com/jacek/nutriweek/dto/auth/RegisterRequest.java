package com.jacek.nutriweek.dto.auth;


public record RegisterRequest(String login, String email, String password) {
    @Override
    public String toString() {
        return "Login: " + login + "\n" +
                "Email: " + email + "\n" +
                "Password: " + password + "\n";
    }
}
