package dev.edu.keycloak;

import dev.edu.keycloak.model.User;
import dev.edu.keycloak.service.KeycloakUserService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class KeycloakApplicationTests
{
	@Autowired
	private KeycloakUserService keycloakUserService;
	@Autowired
	private MockMvc mockMvc;

//	@Test
//	void contextLoads()
//	{}

	@Test
	public void testCreateUser() throws Exception
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
		Response response = Response.status(201).header("Location", "http://localhost:8080/keycloak/api/users/123").build();

		String locationHeader = response.getHeaderString("Location");
		String createdUserId = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);

		// Mock the dependencies
		when(keycloakUserService.mapUserRep(user)).thenReturn(userRep);
		when(keycloakUserService.getUsersResource().create(userRep)).thenReturn(response);

		// Act
		ResponseEntity<User> result = keycloakUserService.createUser(user);

		// Assert
		assertEquals(HttpStatus.CREATED, result.getStatusCode());
		assertTrue(isValidUUID(result.getBody().getId()));
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
