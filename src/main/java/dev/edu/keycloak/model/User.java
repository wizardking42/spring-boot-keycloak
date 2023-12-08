package dev.edu.keycloak.model;

import java.util.Map;

public class User
{
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean emailVerified;
    private Map<String, String> credentials;
    private Map<String, String> attributes;
    private boolean enabled;
}
