package dev.edu.keycloak.service;

import dev.edu.keycloak.config.KeycloakConfig;
import dev.edu.keycloak.model.User;
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
    public ResponseEntity<User> createUser(User userRegistrationRecord)
    {
        UserRepresentation user = mapUserRep(userRegistrationRecord);

        UsersResource usersResource = getUsersResource();
        Response response = usersResource.create(user);

        // Assign role to new user
        assignRole(user);

        // Retrieve created user's ID from the Location header
        String locationHeader = response.getHeaderString("Location");
        String createdUserId = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);

        // Assign created user's ID to the userRegistrationRecord object
        userRegistrationRecord.setId(createdUserId);

        if (Objects.equals(response.getStatus(), 201))
        {
            return new ResponseEntity<>(userRegistrationRecord, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
    }

    @Override
    public User getUserById(String userId)
    {
        UsersResource usersResource = getUsersResource();

        try
        {
            UserRepresentation userRepresentation = usersResource.get(userId).toRepresentation();
            return mapUser(userRepresentation);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getUsers()
    {
        try
        {
            List<UserRepresentation> userRepresentations = getUsersResource().list();
            return mapUsers(userRepresentations);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User updateUser(User user)
    {
        // NOT TESTED
        UserRepresentation userRep = mapUserRep(user);

        UsersResource usersResource = getUsersResource();
        try
        {
            usersResource.get(userRep.getId()).update(userRep);
            return user;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Response deleteUserById(String userId)
    {
        UsersResource usersResource = getUsersResource();
        try
        {
            usersResource.delete(userId);
        }
        catch (Exception e)
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().build();
    }

    // ========================================== Helper Methods ==========================================
    private UsersResource getUsersResource()
    {
        Keycloak kc = keycloakConfig.getKc_demoClient_instance();
        return kc.realm(realm).users();
    }

    private void assignRole(UserRepresentation user)
    {
        Keycloak kc = keycloakConfig.getKc_demoClient_instance();

        String id = kc.realm(realm).users().search(user.getUsername()).get(0).getId();
        RoleRepresentation roleRep = kc.realm(realm).roles().get("user").toRepresentation();
        kc.realm(realm).users().get(id).roles().realmLevel().add(Arrays.asList(roleRep));
    }

    private UserRepresentation mapUserRep(User userRegistrationRecord)
    {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userRegistrationRecord.getUsername());
        user.setEmail(userRegistrationRecord.getEmail());
        user.setFirstName(userRegistrationRecord.getFirstName());
        user.setLastName(userRegistrationRecord.getLastName());
        user.setEmailVerified(true);
        user.setEnabled(true);

        List<CredentialRepresentation> credentials = new ArrayList<>();
        CredentialRepresentation credentialRepresentation = getCredentialRepresentation(userRegistrationRecord);
        credentials.add(credentialRepresentation);
        user.setCredentials(credentials);

        return user;
    }

    private static CredentialRepresentation getCredentialRepresentation(User user)
    {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(user.getPassword());
        credentialRepresentation.setTemporary(false);
        return credentialRepresentation;
    }

    // Method that converts a UserRepresentation object to a UserRegistrationRecord object
    private User mapUser(UserRepresentation userRep)
    {
        User userRegRec = new User(
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
    private List<User> mapUsers(List<UserRepresentation> userRep)
    {
        List<User> users = new ArrayList<>();

        for (UserRepresentation userRepresentation : userRep)
        {
            User userRegRec = mapUser(userRepresentation);

            users.add(userRegRec);
        }
        return users;
    }
}
