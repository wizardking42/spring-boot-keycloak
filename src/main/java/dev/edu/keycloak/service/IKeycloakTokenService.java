package dev.edu.keycloak.service;

import org.keycloak.representations.AccessTokenResponse;

public interface IKeycloakTokenService
{
    AccessTokenResponse getAdminAccessToken();
}
