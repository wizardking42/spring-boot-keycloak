package dev.edu.keycloak.controller;

import dev.edu.keycloak.model.UserRegistrationRecord;
import dev.edu.keycloak.service.KeycloakUserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/keycloak/api/users")
public class KeycloakUserApi
{
    private final KeycloakUserService keycloakUserService;

    @Autowired
    public KeycloakUserApi(KeycloakUserService keycloakUserService)
    {
        this.keycloakUserService = keycloakUserService;
    }

    @PostMapping
    public UserRegistrationRecord createUser(@RequestBody UserRegistrationRecord userRegistrationRecord)
    {
        return keycloakUserService.createUser(userRegistrationRecord);
    }

    @GetMapping
    public UserRepresentation getUser(Principal principal)
    {
        return keycloakUserService.getUserById(principal.getName());
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable String userId)
    {
        keycloakUserService.deleteUserById(userId);
    }
}
