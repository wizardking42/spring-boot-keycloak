package dev.edu.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@SecurityScheme(
//		name = "Keycloak",
//		openIdConnectUrl = "http://localhost:8080/realms/myrealm/.well-known/openid-configuration",
//		scheme = "bearer",
//		type = SecuritySchemeType.OPENIDCONNECT,
//		in = SecuritySchemeIn.HEADER
//)
public class KeycloakApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(KeycloakApplication.class, args);
	}
}
