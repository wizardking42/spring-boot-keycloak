package dev.edu.keycloak.service;

import dev.edu.keycloak.model.UserRegistrationRecord;
import jakarta.ws.rs.core.Response;

import java.util.List;

public interface IKeycloakUserService
{
    int createUser(UserRegistrationRecord user);
    UserRegistrationRecord getUserById(String userId);
    List<UserRegistrationRecord> getUsers();
    void deleteUserById(String userId);
}
