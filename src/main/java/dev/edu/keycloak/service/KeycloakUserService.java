package dev.edu.keycloak.service;

import dev.edu.keycloak.config.KeycloakConfig;
import dev.edu.keycloak.model.UserRegistrationRecord;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeycloakUserService implements IKeycloakUserService
{
    private KeycloakConfig keycloakConfig;
    @Value("${keycloak.realm}")
    private String realm;

    @Autowired
    public KeycloakUserService(KeycloakConfig keycloakConfig)
    {
        this.keycloakConfig = keycloakConfig;
    }


    @Override
    public Response createUser(UserRegistrationRecord userRegistrationRecord)
    {
        UserRepresentation user = mapUserRep(userRegistrationRecord);

        UsersResource usersResource = getUsersResource();
        Response response = usersResource.create(user);

//        if (Objects.equals(response.getStatus(), 201))
//        {
//            return userRegistrationRecord;
//        }
//        return null;
        return response;
    }

    @Override
    public UserRegistrationRecord getUserById(String userId)
    {
        UsersResource usersResource = getUsersResource();

        UserRepresentation userRepresentation = usersResource.get(userId).toRepresentation();

        return mapUser(userRepresentation);
    }

    @Override
    public List<UserRegistrationRecord> getUsers()
    {
        List<UserRepresentation> userRepresentations = getUsersResource().list();

        return mapUsers(userRepresentations);
    }

    @Override
    public void deleteUserById(String userId)
    {
        UsersResource usersResource = getUsersResource();
        usersResource.delete(userId);
    }

    // ================================ Helper Methods ================================
    private UserRepresentation mapUserRep(UserRegistrationRecord userRegistrationRecord)
    {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userRegistrationRecord.username());
        user.setEmail(userRegistrationRecord.email());
        user.setFirstName(userRegistrationRecord.firstName());
        user.setLastName(userRegistrationRecord.lastName());
        user.setEmailVerified(true);
        user.setEnabled(true);

        List<CredentialRepresentation> credentials = new ArrayList<>();
        CredentialRepresentation credentialRepresentation = getCredentialRepresentation(userRegistrationRecord);
        credentials.add(credentialRepresentation);
        user.setCredentials(credentials);

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
//        RealmResource realmResource = keycloak.realm(realm);
//        return realmResource.users();
        Keycloak kc = keycloakConfig.getKeycloakInstance();
        return kc.realm(realm).users();
    }

    // Method that converts a UserRepresentation object to a UserRegistrationRecord object
    private UserRegistrationRecord mapUser(UserRepresentation userRep)
    {
        UserRegistrationRecord userRegRec = new UserRegistrationRecord(
                userRep.getUsername(),
                userRep.getFirstName(),
                userRep.getLastName(),
                userRep.getEmail(),
                null
        );
        return userRegRec;
    }

    // Method that converts a list of UserRepresentation objects to a list of UserRegistrationRecord objects
    private List<UserRegistrationRecord> mapUsers(List<UserRepresentation> userRep)
    {
        List<UserRegistrationRecord> userRegistrationRecords = new ArrayList<>();

        for (UserRepresentation userRepresentation : userRep)
        {
            UserRegistrationRecord userRegRec = mapUser(userRepresentation);

            userRegistrationRecords.add(userRegRec);
        }
        return userRegistrationRecords;
    }
}
