package dev.edu.keycloak.service;

import dev.edu.keycloak.config.KeycloakConfig;
import dev.edu.keycloak.model.UserRegistrationRecord;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
    public ResponseEntity<UserRegistrationRecord> createUser(UserRegistrationRecord userRegistrationRecord)
    {
        UserRepresentation user = mapUserRep(userRegistrationRecord);

        UsersResource usersResource = getUsersResource();
        Response response = usersResource.create(user);

        // Assign role to new user
        assignRole(user);

        if (Objects.equals(response.getStatus(), 201))
        {
            return new ResponseEntity<>(userRegistrationRecord, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
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
    public UserRegistrationRecord updateUser(UserRegistrationRecord user)
    {
        UserRepresentation userRep = mapUserRep(user);

        UsersResource usersResource = getUsersResource();
        usersResource.get(userRep.getId()).update(userRep);

        return user;
    }

    @Override
    public Response deleteUserById(String userId)
    {
        UsersResource usersResource = getUsersResource();
        usersResource.delete(userId);

        return Response.ok().build();
    }

    // ================================ Helper Methods ================================
    private UsersResource getUsersResource()
    {
//        RealmResource realmResource = keycloak.realm(realm);
//        return realmResource.users();
        Keycloak kc = keycloakConfig.getKeycloakInstance();
        return kc.realm(realm).users();
    }

    private void assignRole(UserRepresentation user)
    {
        Keycloak kc = keycloakConfig.getKeycloakInstance();

        String id = kc.realm(realm).users().search(user.getUsername()).get(0).getId();
        RoleRepresentation roleRep = kc.realm(realm).roles().get("user").toRepresentation();
        kc.realm(realm).users().get(id).roles().realmLevel().add(Arrays.asList(roleRep));
    }

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

    // Method that converts a UserRepresentation object to a UserRegistrationRecord object
    private UserRegistrationRecord mapUser(UserRepresentation userRep)
    {
        UserRegistrationRecord userRegRec = new UserRegistrationRecord(
                userRep.getId(),
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
