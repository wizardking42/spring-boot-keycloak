package dev.edu.keycloak.service;

import dev.edu.keycloak.model.UserRegistrationRecord;
import org.keycloak.representations.idm.UserRepresentation;

public interface IKeycloakUserService
{
    UserRegistrationRecord createUser(UserRegistrationRecord user);
    UserRepresentation getUserById(String userId);
    void deleteUserById(String userId);
}
