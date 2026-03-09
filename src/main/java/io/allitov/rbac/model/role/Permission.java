package io.allitov.rbac.model.role;

import io.allitov.rbac.model.validator.Validator;
import io.allitov.rbac.model.validator.impl.PermissionValidator;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Permission {

    @Getter(AccessLevel.NONE)
    private static final Validator<Permission> VALIDATOR = new PermissionValidator();

    private final String name;
    private final String resource;
    private final String description;

    public static Permission of(String name, String resource, String description) {
        name = StringUtils.toRootUpperCase(name);
        resource = StringUtils.toRootLowerCase(resource);
        Permission newPermission = new Permission(name, resource, description);
        VALIDATOR.validate(newPermission);
        return newPermission;
    }

    public String format() {
        return "%s on %s: %s".formatted(name, resource, description);
    }

    public boolean matches(String namePattern, String resourcePattern) {
        return Strings.CI.contains(name, namePattern) && Strings.CI.contains(resource, resourcePattern);
    }
}
