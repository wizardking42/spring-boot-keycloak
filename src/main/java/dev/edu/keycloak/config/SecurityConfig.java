package dev.edu.keycloak.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
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
//        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
//        requestHandler.setCsrfRequestAttributeName("_csrf");


        http
//                .csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler)
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET, "api/v1/demo/hello").hasRole("user")
                        .requestMatchers(HttpMethod.GET, "api/v1/demo/hello2").hasRole("admin")
                        .requestMatchers(HttpMethod.POST, "keycloak/api/users").permitAll()
                        //.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        //.requestMatchers(HttpMethod.GET, "keycloak/api/users").permitAll()
                        //.requestMatchers(HttpMethod.DELETE, "keycloak/api/users/{userId}").permitAll()
                        .anyRequest().authenticated()
                );
        http
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));
//        http
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    // Removes need for appending "ROLE_" to role names (in Method Level Security ONLY) (Cf. JwtAuthConverter)
//    @Bean
//    public DefaultMethodSecurityExpressionHandler msecurity()
//    {
//        DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler = new DefaultMethodSecurityExpressionHandler();
//        defaultMethodSecurityExpressionHandler.setDefaultRolePrefix("");
//        return defaultMethodSecurityExpressionHandler;
//    }
}
