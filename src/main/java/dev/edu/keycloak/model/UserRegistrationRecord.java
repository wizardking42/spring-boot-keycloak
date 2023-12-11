package dev.edu.keycloak.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record UserRegistrationRecord(
        String username,
        String firstName,
        String lastName,
        String email,
        @JsonIgnore String password) {}
