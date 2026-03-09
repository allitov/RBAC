package io.allitov.rbac.sorter;

import static org.assertj.core.api.Assertions.assertThat;

import io.allitov.rbac.model.role.Permission;
import io.allitov.rbac.model.role.Role;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoleSortersTest {

    @Test
    @DisplayName("Должен сортировать роли по имени (алфавитный порядок)")
    void shouldSortByName() {
        Role r1 = Role.of("ADMIN", "Admin role");
        Role r2 = Role.of("GUEST", "Guest role");
        Role r3 = Role.of("USER", "User role");
        List<Role> roles = new ArrayList<>(List.of(r3, r1, r2));

        roles.sort(RoleSorters.byName());

        assertThat(roles).containsExactly(r1, r2, r3);
    }

    @Test
    @DisplayName("Должен сортировать роли по количеству разрешений")
    void shouldSortByPermissionCount() {
        Role r1 = Role.of("NO_PERM", "Empty");
        Role r2 = Role.of("ONE_PERM", "One");
        Role r3 = Role.of("TWO_PERM", "Two");

        r2.addPermission(Permission.of("READ", "system", "desc"));
        r3.addPermission(Permission.of("READ", "system", "desc"));
        r3.addPermission(Permission.of("WRITE", "system", "desc"));

        List<Role> roles = new ArrayList<>(List.of(r3, r1, r2));

        roles.sort(RoleSorters.byPermissionCount());

        assertThat(roles).containsExactly(r1, r2, r3);
    }
}
