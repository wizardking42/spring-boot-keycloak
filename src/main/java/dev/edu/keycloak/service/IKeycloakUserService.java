package dev.edu.keycloak.service;

import dev.edu.keycloak.model.User;
import jakarta.ws.rs.core.Response;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IKeycloakUserService
{
    ResponseEntity<User> createUser(User user);
    UserRepresentation getUserById(String userId);
    List<User> getUsers();
    User updateUser(String userId, User user);
    Response deleteUserById(String userId);
    String disableUser(String userId);
    String enableUser(String userId);
}
