package dev.edu.keycloak.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig
{
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.urls.auth}")
    private String authServerUrl;
    @Value("{keycloak.adminClientId}")
    private String adminClientId;
    @Value("{keycloak.adminClientSecret}")
    private String adminClientSecret;

    @Bean
    public Keycloak keycloak()
    {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(adminClientId)
                .clientSecret(adminClientSecret)
                .build();

        //keycloak.tokenManager().getAccessToken();
        return keycloak;
    }
}
