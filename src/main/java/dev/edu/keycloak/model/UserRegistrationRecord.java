package dev.edu.keycloak.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserRegistrationRecord(
        String id,
        String username,
        String firstName,
        String lastName,
        String email,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String password) {}
