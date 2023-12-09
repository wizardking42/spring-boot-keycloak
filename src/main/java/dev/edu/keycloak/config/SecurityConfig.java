package dev.edu.keycloak.config;

import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.adapters.authorization.spi.ConfigurationResolver;
import org.keycloak.adapters.authorization.spi.HttpRequest;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.util.JsonSerialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
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
//        http
//                .addFilterAfter(createPolicyEnforcerFilter(), BearerTokenAuthenticationFilter.class);

        return http.build();
    }

    // Removes need for appending "ROLE_" to role names (for Method Level Security) (Cf. JwtAuthConverter)
//    @Bean
//    public DefaultMethodSecurityExpressionHandler msecurity()
//    {
//        DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler = new DefaultMethodSecurityExpressionHandler();
//        defaultMethodSecurityExpressionHandler.setDefaultRolePrefix("");
//        return defaultMethodSecurityExpressionHandler;
//    }

    private ServletPolicyEnforcerFilter createPolicyEnforcerFilter()
    {
        return new ServletPolicyEnforcerFilter(new ConfigurationResolver()
        {
            @Override
            public PolicyEnforcerConfig resolve(HttpRequest request)
            {
                try
                {
                    return JsonSerialization.readValue(getClass().getResourceAsStream("/policy-enforcer.json"), PolicyEnforcerConfig.class);
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer()
//    {
//        return (web) -> {
//            web.ignoring().requestMatchers(
//                    HttpMethod.POST,
//                    "/public/**",
//                    "keycloak/api/users"
//            );
//        };
//    }
}
