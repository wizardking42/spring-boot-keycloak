package dev.edu.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// Swagger-UI
//@SecurityScheme(
//		name = "Keycloak",
//		openIdConnectUrl = "http://localhost:8080/realms/myrealm/.well-known/openid-configuration",
//		scheme = "bearer",
//		type = SecuritySchemeType.OPENIDCONNECT,
//		in = SecuritySchemeIn.HEADER
//)
@SpringBootApplication
public class KeycloakApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(KeycloakApplication.class, args);
	}
}


/*
TODO: Get access token from Keycloak
    * 1. Retrieve access token from Keycloak
    	- KeycloakTokenService.java > getAdminAccessToken() - TEST
    * 2. Include access token in the header of the request???
    * 3. Pass username & pw???

TODO: Update user
	* 1. Update user
		- KeycloakUserService.java > updateUser()
*/
