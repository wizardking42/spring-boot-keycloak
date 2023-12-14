package dev.edu.keycloak.service;

import dev.edu.keycloak.model.User;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KeycloakUserServiceTests
{
    //private userResource;

    @InjectMocks
    private KeycloakUserService keycloakUserService;

    @Test
    public void KcUserService_CreateUser_ReturnsResponseEntity()
    {
        // Arrange
        User user = new User(
                "test_id",
                "test_username",
                "test_firstName",
                "test_lastName",
                "test_email",
                "test_pw");
        UserRepresentation userRep = keycloakUserService.mapUserRep(user);
        ResponseEntity<User> expected_response = new ResponseEntity<>(user, HttpStatus.CREATED);
        Response response = Response.status(201).header("Location", "http://localhost:8080/keycloak/api/users/123").build();

        // Mock the dependencies
        when(keycloakUserService.mapUserRep(Mockito.any(User.class))).thenReturn(userRep);
        when(keycloakUserService.getUsersResource().create(Mockito.any(UserRepresentation.class))).thenReturn(response);

        // Act
        ResponseEntity<User> actual_response = keycloakUserService.createUser(user);

        // Assert
        assertEquals(expected_response, actual_response);
    }

    // ========================================== Helper Methods ==========================================
    public static boolean isValidUUID(String uuid)
    {
        // Method 1:
        try
        {
            UUID.fromString(uuid);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

        // Method 2:
        //String regex = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";
//        String regex = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";
//        return uuid.matches(regex);
    }
}
