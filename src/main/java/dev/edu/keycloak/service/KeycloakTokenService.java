package dev.edu.keycloak.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class KeycloakTokenService implements IKeycloakTokenService
{
    @Value("${keycloak.server-url}")
    private String serverUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.client-id}")
    private String clientId;
    @Value("${keycloak.client-secret}")
    private String clientSecret;
    @Value("${keycloak.admin-username}")
    private String admin_username;
    @Value("${keycloak.admin-password}")
    private String admin_password;

    public Mono<String> getAdminAccessToken()
    {
        WebClient webClient = WebClient.create();
        String url = "%s/realms/$s/protocol/openid-connect/token".formatted(serverUrl, realm);
        return webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(
                                "client_id=$s&" +
                                "client_secret=$s&" +
                                "grant_type=password&" +
                                "username=$s&" +
                                "password=$s&" +
                                "scope=openid".formatted(clientId, clientSecret, admin_username, admin_password)
                )
                .retrieve()
                .bodyToMono(String.class);
    }
}
