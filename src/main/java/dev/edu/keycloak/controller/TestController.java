package dev.edu.keycloak.controller;

import dev.edu.keycloak.service.KeycloakTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("keycloak/api/test")
public class TestController
{
    KeycloakTokenService keycloakTokenService;

    @Autowired
    public TestController(KeycloakTokenService keycloakTokenService)
    {
        this.keycloakTokenService = keycloakTokenService;
    }

    @GetMapping("/getAccessToken")
    public Mono<String> getAccessToken()
    {
        return keycloakTokenService.getAdminAccessToken();
    }
}
