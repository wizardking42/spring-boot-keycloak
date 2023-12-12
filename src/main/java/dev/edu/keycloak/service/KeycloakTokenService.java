package dev.edu.keycloak.service;

import dev.edu.keycloak.config.KeycloakConfig;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeycloakTokenService implements IKeycloakTokenService
{
    KeycloakConfig keycloakConfig;

    @Autowired
    public KeycloakTokenService(KeycloakConfig keycloakConfig)
    {
        this.keycloakConfig = keycloakConfig;
    }

    public AccessTokenResponse getAdminAccessToken()
    {
        return keycloakConfig.getKc_demoClient_instance().tokenManager().getAccessToken();
    }
}
