package io.allitov.rbac.model.role;

import io.allitov.rbac.validator.Validator;
import io.allitov.rbac.validator.impl.RoleValidator;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role {

    @Getter(AccessLevel.NONE)
    private static final Validator<Role> VALIDATOR = new RoleValidator();

    @EqualsAndHashCode.Include
    private final String id = "role_" + UUID.randomUUID();

    private final String name;
    private final String description;
    private final Set<Permission> permissions = new HashSet<>();

    public static Role of(String name, String description) {
        Role newRole = new Role(name, description);
        VALIDATOR.validate(newRole);
        return newRole;
    }

    public void addPermission(Permission permission) {
        permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        permissions.remove(permission);
    }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    public boolean hasPermission(String permissionName, String resource) {
        return permissions.stream().anyMatch(permission -> permission.matches(permissionName, resource));
    }

    public String format() {
        StringBuilder sb = new StringBuilder();
        sb.append("Role: %s [ID: %s]%n".formatted(name, id));
        sb.append("Description: %s%n".formatted(description));
        sb.append("Permissions: (%s):%n".formatted(permissions.size()));
        permissions.forEach(p -> sb.append("\t- %s%n".formatted(p.format())));

        return sb.toString();
    }

    public Set<Permission> getPermissions() {
        return Set.copyOf(permissions);
    }
}
