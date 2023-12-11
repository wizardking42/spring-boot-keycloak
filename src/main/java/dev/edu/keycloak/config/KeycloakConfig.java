package dev.edu.keycloak.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Configuration
@Component
public class KeycloakConfig
{
    Keycloak keycloak;
    @Value("${keycloak.server-url}")
    private String authServerUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.adminCli}")
    private String adminCli;
    @Value("${keycloak.admin-username}")
    private String username;
    @Value("${keycloak.admin-password}")
    private String password;

    //@Bean
    public Keycloak getKeycloakInstance()
    {
        if (keycloak == null)
        {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(authServerUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(adminCli)
                    .username(username)
                    .password(password)
                    .build();
        }

        return keycloak;
    }
}
