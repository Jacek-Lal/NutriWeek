package com.jacek.nutriweek.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@NotBlank @Size(min = 5, max = 30) String login,
                              @NotBlank @Email String email,
                              @NotBlank @Size(min = 8) String password) {}
