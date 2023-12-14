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
    public ResponseEntity<?> createUser(User user)
    {
        UserRepresentation userRep = mapUserRep(user);

        UsersResource usersResource = getUsersResource();

        try
        {
            Response response = usersResource.create(userRep);
            // Assign role to new userRep
            assignRole(userRep);

            // Retrieve created userRep's ID from the Location header
            String locationHeader = response.getHeaderString("Location");

            // Duplicate handling
            if (locationHeader == null)
                return new ResponseEntity<>("Duplicate user", HttpStatus.valueOf(response.getStatus()));

            String createdUserId = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);

            // Assign created userRep's ID to the user object
            user.setId(createdUserId);

            if (Objects.equals(response.getStatus(), 201))
            {
                return new ResponseEntity<>(user, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
        catch (Exception e)
        {
            //throw new RuntimeException(e);
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserRepresentation getUserById(String userId)
    {
        UsersResource usersResource = getUsersResource();
        try
        {
            UserRepresentation userRepresentation = usersResource.get(userId).toRepresentation();
            return userRepresentation;
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

    // This method can only be used to update the user's first name, last name, and/or email.
    @Override
    public User updateUser(String userId, User user)
    {
        UserRepresentation userRep = getUserById(userId);

        try
        {
            if (user.getFirstName() != null && !user.getFirstName().isEmpty())
                userRep.setFirstName(user.getFirstName());
            if (user.getLastName() != null && !user.getLastName().isEmpty())
                userRep.setLastName(user.getLastName());
            if (user.getEmail() != null && !user.getEmail().isEmpty())
                userRep.setEmail(user.getEmail());

            UsersResource usersResource = getUsersResource();
            usersResource.get(userId).update(userRep);
            return new User(
                    userRep.getId(),
                    userRep.getUsername(),
                    userRep.getFirstName(),
                    userRep.getLastName(),
                    userRep.getEmail(),
                    null,
                    userRep.isEnabled());
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

    @Override
    public String enableUser(String userId)
    {
        return changeUserStatus(userId, true);
    }

    @Override
    public String disableUser(String userId)
    {
        return changeUserStatus(userId, false);
    }

    // ========================================== Helper Methods ==========================================
    public UsersResource getUsersResource()
    {
        try
        {
            Keycloak kc = keycloakConfig.getKc_demoClient_instance();
            return kc.realm(realm).users();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private void assignRole(UserRepresentation user)
    {
        Keycloak kc = keycloakConfig.getKc_demoClient_instance();

        String id = kc.realm(realm).users().search(user.getUsername()).get(0).getId();
        RoleRepresentation roleRep = kc.realm(realm).roles().get("user").toRepresentation();
        kc.realm(realm).users().get(id).roles().realmLevel().add(Arrays.asList(roleRep));
    }

    private String changeUserStatus(String userId, boolean status)
    {
        String enabled_or_disabled = status ? "enabled" : "disabled";

        UserRepresentation userRep = getUserById(userId);
        userRep.setEnabled(status);
        //User user = mapUser(userRep);

        UsersResource usersResource = getUsersResource();
        try
        {
            usersResource.get(userId).update(userRep);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return "User " + userRep.getUsername() + " has been " + enabled_or_disabled + " successfully!";
    }

    public UserRepresentation mapUserRep(User user)
    {
        try
        {
            UserRepresentation userRep = new UserRepresentation();
            userRep.setUsername(user.getUsername());
            userRep.setEmail(user.getEmail());
            userRep.setFirstName(user.getFirstName());
            userRep.setLastName(user.getLastName());
            userRep.setEmailVerified(true);
            userRep.setEnabled(user.isEnabled());

            List<CredentialRepresentation> credentials = new ArrayList<>();
            CredentialRepresentation credentialRepresentation = getCredentialRepresentation(user);
            credentials.add(credentialRepresentation);
            userRep.setCredentials(credentials);

            return userRep;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private static CredentialRepresentation getCredentialRepresentation(User user)
    {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(user.getPassword());
        credentialRepresentation.setTemporary(false);
        return credentialRepresentation;
    }

    // Method that converts a UserRepresentation object to a User object
    public User mapUser(UserRepresentation userRep)
    {
        try
        {
            User user = new User(
                    userRep.getId(),
                    userRep.getUsername(),
                    userRep.getFirstName(),
                    userRep.getLastName(),
                    userRep.getEmail(),
                    null,
                    userRep.isEnabled()
            );
            return user;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    // Method that converts a list of UserRepresentation objects to a list of User objects
    private List<User> mapUsers(List<UserRepresentation> userReps)
    {
        List<User> users = new ArrayList<>();

        for (UserRepresentation userRep : userReps)
        {
            User user = mapUser(userRep);

            users.add(user);
        }
        return users;
    }
}
