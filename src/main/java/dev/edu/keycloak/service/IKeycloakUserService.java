package dev.edu.keycloak.service;

import dev.edu.keycloak.model.UserRegistrationRecord;
import jakarta.ws.rs.core.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IKeycloakUserService
{
    ResponseEntity<UserRegistrationRecord> createUser(UserRegistrationRecord user);
    UserRegistrationRecord getUserById(String userId);
    List<UserRegistrationRecord> getUsers();
    UserRegistrationRecord updateUser(UserRegistrationRecord user);
    Response deleteUserById(String userId);
}
