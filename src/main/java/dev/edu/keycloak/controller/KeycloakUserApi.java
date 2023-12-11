package dev.edu.keycloak.controller;

import dev.edu.keycloak.model.UserRegistrationRecord;
import dev.edu.keycloak.service.KeycloakUserService;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public List<UserRegistrationRecord> getUsers()
    {
        return keycloakUserService.getUsers();
    }

//    @GetMapping("/user")
//    public UserRegistrationRecord getUser(Principal principal)
//    {
//        return keycloakUserService.getUserById(principal.getName());
//    }

    @GetMapping("/{userId}")
    public UserRegistrationRecord getUser(@PathVariable String userId)
    {
        return keycloakUserService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public Response deleteUserById(@PathVariable String userId)
    {
        return keycloakUserService.deleteUserById(userId);
    }
}
