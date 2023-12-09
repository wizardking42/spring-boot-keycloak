package dev.edu.keycloak.service;

import dev.edu.keycloak.model.UserRegistrationRecord;

import java.util.List;

public interface IKeycloakUserService
{
    UserRegistrationRecord createUser(UserRegistrationRecord user);
    UserRegistrationRecord getUserById(String userId);
    List<UserRegistrationRecord> getUsers();
    void deleteUserById(String userId);
}
