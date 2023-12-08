package dev.edu.keycloak.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    private final JwtAuthConverter jwtAuthConverter;

    @Autowired
    public SecurityConfig(JwtAuthConverter jwtAuthConverter)
    {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        http
//                .csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler)
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET, "api/v1/demo/hello").authenticated()
                        .requestMatchers(HttpMethod.GET, "api/v1/demo/hello2").authenticated()
                        .requestMatchers(HttpMethod.POST, "keycloak/api/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "keycloak/api/users").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "keycloak/api/users/{userId}").permitAll());
        http
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer()
    {
        return (web) -> {
            web.ignoring().requestMatchers(
                    HttpMethod.POST,
                    "/keycloak/api/users"
            );
        };
    }
}
