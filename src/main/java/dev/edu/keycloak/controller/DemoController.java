package dev.edu.keycloak.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/demo")
public class DemoController
{

    @GetMapping("/hello")
    //@PreAuthorize("hasRole('client_user')")
    //@PreAuthorize("hasRole('user')")
    public String hello()
    {
        return "Hello from Spring boot & Keycloak";
    }

    @GetMapping("/hello2")
    //@PreAuthorize("hasRole('client_admin')")
    //@PreAuthorize("hasRole('admin')")
    public String hello2()
    {
        return "Hello from Spring boot & Keycloak - ADMIN";
    }
}
