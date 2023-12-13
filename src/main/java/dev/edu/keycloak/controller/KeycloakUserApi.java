package dev.edu.keycloak.controller;

import dev.edu.keycloak.model.User;
import dev.edu.keycloak.service.KeycloakTokenService;
import dev.edu.keycloak.service.KeycloakUserService;
import org.keycloak.representations.idm.UserRepresentation;
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
    private final KeycloakTokenService keycloakTokenService;

    @Autowired
    public KeycloakUserApi(KeycloakUserService keycloakUserService, KeycloakTokenService keycloakTokenService)
    {
        this.keycloakUserService = keycloakUserService;
        this.keycloakTokenService = keycloakTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user)
    {
        return keycloakUserService.createUser(user);
    }

    @GetMapping
    public List<User> getUsers()
    {
        return keycloakUserService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable String userId)
    {
        UserRepresentation userRep = keycloakUserService.getUserById(userId);
        return keycloakUserService.mapUser(userRep);
    }

    @DeleteMapping("/{userId}")
    public String deleteUserById(@PathVariable String userId)
    {
        String username = keycloakUserService.mapUser(keycloakUserService.getUserById(userId)).getUsername();
        keycloakUserService.deleteUserById(userId);
        //return "User with ID: " + userId + " has been deleted successfully!";
        return "User " + username + " has been deleted successfully!";
    }

    @PostMapping("/enable/{userId}")
    public String enableUser(@PathVariable String userId)
    {
        return keycloakUserService.enableUser(userId);
    }

    @PostMapping("/disable/{userId}")
    public String disableUser(@PathVariable String userId)
    {
        return keycloakUserService.disableUser(userId);
    }

//    @GetMapping("/user")
//    public UserRegistrationRecord getUser(Principal principal)
//    {
//        return keycloakUserService.getUserById(principal.getName());
//    }
}
