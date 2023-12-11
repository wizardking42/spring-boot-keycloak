package dev.edu.keycloak.service;

import dev.edu.keycloak.model.User;
import jakarta.ws.rs.core.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IKeycloakUserService
{
    ResponseEntity<User> createUser(User user);
    User getUserById(String userId);
    List<User> getUsers();
    User updateUser(User user);
    Response deleteUserById(String userId);
}
