package dev.edu.keycloak.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class KeycloakTokenService
{
    @Value("${keycloak.server-url}")
    String serverUrl;
    @Value("${keycloak.realm}")
    String realm;
    public Mono<String> getAccessToken()
    {
        WebClient webClient = WebClient.create();
        String url = "%s/realms/$s/protocol/openid-connect/token".formatted(serverUrl, realm);
        return webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(
                        "client_id="
                );

    }
}
