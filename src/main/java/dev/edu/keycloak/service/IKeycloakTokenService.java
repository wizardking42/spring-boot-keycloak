package dev.edu.keycloak.service;

import reactor.core.publisher.Mono;

public interface IKeycloakTokenService
{
    Mono<String> getAdminAccessToken();
}
