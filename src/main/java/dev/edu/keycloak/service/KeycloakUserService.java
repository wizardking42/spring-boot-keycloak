package dev.edu.keycloak.service;

import dev.edu.keycloak.model.UserRegistrationRecord;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class KeycloakUserService implements IKeycloakUserService
{
    @Value("${keycloak.realm}")
    private String realm;
    private Keycloak keycloak;

    @Autowired
    public KeycloakUserService(Keycloak keycloak)
    {
        this.keycloak = keycloak;
    }

    @Override
    public UserRegistrationRecord createUser(UserRegistrationRecord userRegistrationRecord)
    {
        UserRepresentation user = getUserRepresentation(userRegistrationRecord);

        CredentialRepresentation credentialRepresentation = getCredentialRepresentation(userRegistrationRecord);

        List<CredentialRepresentation> credentials = new ArrayList<>();
        credentials.add(credentialRepresentation);
        user.setCredentials(credentials);

        UsersResource usersResource = getUsersResource();
        Response response = usersResource.create(user);

        if (Objects.equals(response.getStatus(), 201))
        {
            return userRegistrationRecord;
        }

        return null;
    }

    @Override
    public UserRepresentation getUserById(String userId)
    {
        UsersResource usersResource = getUsersResource();

        return usersResource.get(userId).toRepresentation();
    }

    @Override
    public void deleteUserById(String userId)
    {
        UsersResource usersResource = getUsersResource();
        usersResource.delete(userId);
    }

    // ================================ Helper Methods ================================
    private static UserRepresentation getUserRepresentation(UserRegistrationRecord userRegistrationRecord)
    {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userRegistrationRecord.username());
        user.setEmail(userRegistrationRecord.email());
        user.setFirstName(userRegistrationRecord.firstName());
        user.setLastName(userRegistrationRecord.lastName());
        user.setEmailVerified(true);
        user.setEnabled(true);
        return user;
    }

    private static CredentialRepresentation getCredentialRepresentation(UserRegistrationRecord userRegistrationRecord)
    {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(userRegistrationRecord.password());
        credentialRepresentation.setTemporary(false);
        return credentialRepresentation;
    }

    private UsersResource getUsersResource()
    {
        RealmResource realmResource = keycloak.realm(realm);
        return realmResource.users();
    }
}
