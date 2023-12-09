package dev.edu.keycloak.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken>
{
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt)
    {
        Collection<GrantedAuthority> roles = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, roles);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt)
    {
        if (jwt.getClaim("realm_access") != null)
        {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            ObjectMapper mapper = new ObjectMapper();
            List<String> keycloakRoles = mapper.convertValue(realmAccess.get("roles"), new TypeReference<List<String>>(){});
            List<GrantedAuthority> roles = new ArrayList<>();

            for (String keyCloakRole : keycloakRoles)
            {
                roles.add(new SimpleGrantedAuthority("ROLE_" + keyCloakRole));
                //roles.add(new SimpleGrantedAuthority(keyCloakRole));
            }

            return roles;
        }
        return new ArrayList<>();
    }


//    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//    private final String principleAttribute = "preferred_username";
//    private final String resourceId = "democlient";
//
//    @Override
//    public AbstractAuthenticationToken convert(@NonNull Jwt jwt)
//    {
//        Collection<GrantedAuthority> authorities = Stream.concat(
//                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
//                extractResourceRoles(jwt).stream()
//        ).collect(Collectors.toSet());
//
//        return new JwtAuthenticationToken(jwt, authorities, getPrincipleClaimName(jwt));
//    }
//
//    private String getPrincipleClaimName(Jwt jwt)
//    {
//        String claimName = JwtClaimNames.SUB;
//        if (principleAttribute != null)
//            claimName = principleAttribute;
//        return jwt.getClaim(claimName);
//    }
//
//    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt)
//    {
//        Map<String, Object> resourceAccess;
//        Map<String, Object> resource;
//        Collection<String> resourceRoles;
//
//        if (jwt.getClaim("resource_access") == null)
//            return Set.of();
//
//        resourceAccess = jwt.getClaim("resource_access");
//
//        if (resourceAccess.get(resourceId) == null)
//            return Set.of();
//
//        resource = (Map<String, Object>) resourceAccess.get(resourceId);
//
//        resourceRoles = (Collection<String>) resource.get("roles");
//
//        return resourceRoles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
//                .collect(Collectors.toSet());
//    }
}
