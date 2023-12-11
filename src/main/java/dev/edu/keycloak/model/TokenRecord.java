package dev.edu.keycloak.model;

public record TokenRecord(
        String access_token,
        String expires_in,
        String refresh_expires_in,
        String refresh_token,
        String token_type,
        String id_token,
        String not_before_policy,
        String session_state,
        String scope
)
{}
