package dev.edu.keycloak.service;

import dev.edu.keycloak.model.Role;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.ArrayList;
import java.util.List;

public class KeycloakRoleService implements IKeycloakRoleService
{
    public static List<Role> mapRoles(List<RoleRepresentation> representations)
    {
        List<Role> roles = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(representations)) {
            representations.forEach(roleRep -> roles.add(mapRole(roleRep)));
        }
        return roles;
    }

    public static Role mapRole(RoleRepresentation roleRep)
    {
        Role role = new Role();
        role.setId(roleRep.getId());
        role.setName(roleRep.getName());
        return role;
    }

    public RoleRepresentation mapRoleRep(Role role)
    {
        RoleRepresentation roleRep = new RoleRepresentation();
        roleRep.setName(role.getName());
        return roleRep;
    }
}
