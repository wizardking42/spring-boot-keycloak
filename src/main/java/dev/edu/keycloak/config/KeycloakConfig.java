package dev.edu.keycloak.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakConfig
{
    Keycloak keycloak;
    @Value("${keycloak.server-url}")
    private String serverUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.adminCli}")
    private String adminCli;
    @Value("${keycloak.admin-username}")
    private String admin_username;
    @Value("${keycloak.admin-password}")
    private String admin_pw;

    @Value("${keycloak.client-id}")
    private String clientId;
    @Value("${keycloak.client-secret}")
    private String clientSecret;


    public Keycloak getKc_demoClient_instance()
    {
        if (keycloak == null)
        {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .username(admin_username)
                    .password(admin_pw)
                    .build();
        }

        return keycloak;
    }

    public Keycloak getKc_demoClient_instance(String username, String pw)
    {
        if (keycloak == null)
        {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .username(username)
                    .password(pw)
                    .build();
        }

        return keycloak;
    }

//    public Keycloak getKc_adminCli_instance()
//    {
//        if (keycloak == null)
//        {
//            keycloak = KeycloakBuilder.builder()
//                    .serverUrl(serverUrl)
//                    .realm(realm)
//                    .grantType(OAuth2Constants.PASSWORD)
//                    .clientId(adminCli)
//                    .username(admin_username)
//                    .password(admin_pw)
//                    .build();
//        }
//
//        return keycloak;
//    }
}
