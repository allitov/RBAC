package io.allitov.rbac.filter.role;

import static org.assertj.core.api.Assertions.assertThat;

import io.allitov.rbac.model.role.Permission;
import io.allitov.rbac.model.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoleFiltersTest {

    private Role adminRole;
    private Role guestRole;
    private Permission readPerm;

    @BeforeEach
    void setUp() {
        adminRole = Role.of("ADMIN", "System administrator");
        guestRole = Role.of("GUEST", "Guest access");

        readPerm = Permission.of("READ", "system", "Read access");
        adminRole.addPermission(readPerm);
    }

    @Test
    @DisplayName("Фильтр byName должен проверять точное совпадение имени")
    void shouldFilterByName() {
        RoleFilter filter = RoleFilters.byName("ADMIN");

        assertThat(filter.test(adminRole)).isTrue();
        assertThat(filter.test(guestRole)).isFalse();
    }

    @Test
    @DisplayName("Фильтр hasPermission должен проверять наличие разрешения")
    void shouldFilterByPermission() {
        RoleFilter filter = RoleFilters.hasPermission(readPerm);

        assertThat(filter.test(adminRole)).isTrue();
        assertThat(filter.test(guestRole)).isFalse();
    }

    @Test
    @DisplayName("Фильтр hasAtLeastNPermissions должен проверять количество разрешений")
    void shouldFilterByPermissionCount() {
        RoleFilter filter = RoleFilters.hasAtLeastNPermissions(1);

        assertThat(filter.test(adminRole)).isTrue();
        assertThat(filter.test(guestRole)).isFalse();
    }

    @Test
    @DisplayName("Проверка логического 'И' (AND) для цепочки фильтров")
    void shouldComposeFiltersWithAnd() {
        RoleFilter isAdmin = RoleFilters.byName("ADMIN");
        RoleFilter hasPerm = RoleFilters.hasPermission(readPerm);

        RoleFilter combined = isAdmin.and(hasPerm);

        assertThat(combined.test(adminRole)).isTrue();
        assertThat(combined.test(guestRole)).isFalse();
    }
}
