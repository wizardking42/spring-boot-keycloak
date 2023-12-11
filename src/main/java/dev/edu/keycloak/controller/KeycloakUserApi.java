package dev.edu.keycloak.controller;

import dev.edu.keycloak.model.UserRegistrationRecord;
import dev.edu.keycloak.service.KeycloakUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("keycloak/api/users")
//@SecurityRequirement(name = "Keycloak") // Swagger-UI
public class KeycloakUserApi
{
    private final KeycloakUserService keycloakUserService;

    @Autowired
    public KeycloakUserApi(KeycloakUserService keycloakUserService)
    {
        this.keycloakUserService = keycloakUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationRecord> createUser(@RequestBody UserRegistrationRecord userRegistrationRecord)
    {
        return keycloakUserService.createUser(userRegistrationRecord);
    }

    @GetMapping("/user")
    public UserRegistrationRecord getUser(Principal principal)
    {
        return keycloakUserService.getUserById(principal.getName());
    }

    @GetMapping
    public List<UserRegistrationRecord> getUsers()
    {
        return keycloakUserService.getUsers();
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable String userId)
    {
        keycloakUserService.deleteUserById(userId);
    }
}
