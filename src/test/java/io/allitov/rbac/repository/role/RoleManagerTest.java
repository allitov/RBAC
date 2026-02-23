package io.allitov.rbac.repository.role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.allitov.rbac.model.role.Permission;
import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.sorter.role.RoleSorters;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoleManagerTest {

    private RoleManager roleManager;
    private Role adminRole;
    private Role userRole;

    @BeforeEach
    void setUp() {
        roleManager = new RoleManager();
        roleManager.clear();

        adminRole = new Role("ADMIN", "System Administrator");
        userRole = new Role("USER", "Regular User");
    }

    @Test
    @DisplayName("Should add role and find it by generated ID and name")
    void shouldAddRoleToAllIndexes() {
        roleManager.add(adminRole);

        String adminId = adminRole.getId();

        assertThat(roleManager.findById(adminId)).isPresent().contains(adminRole);

        assertThat(roleManager.findByName("ADMIN")).isPresent().contains(adminRole);
    }

    @Test
    @DisplayName("Should throw exception on duplicate ID")
    void shouldThrowOnDuplicateId() {
        roleManager.add(adminRole);

        assertThatThrownBy(() -> roleManager.add(adminRole))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Duplicate role: " + adminRole.getId());
    }

    @Test
    @DisplayName("Should remove role correctly from both maps")
    void shouldRemoveFromAllIndexes() {
        roleManager.add(adminRole);
        String adminId = adminRole.getId();

        boolean removed = roleManager.remove(adminRole);

        assertThat(removed).isTrue();
        assertThat(roleManager.findById(adminId)).isEmpty();
        assertThat(roleManager.findByName("ADMIN")).isEmpty();
    }

    @Test
    @DisplayName("Should add and remove permissions within a role")
    void shouldManagePermissions() {
        roleManager.add(adminRole);
        Permission readPerm = new Permission("READ", "FILES", "desc");

        roleManager.addPermissionToRole("ADMIN", readPerm);
        assertThat(adminRole.hasPermission(readPerm)).isTrue();

        roleManager.removePermissionFromRole("ADMIN", readPerm);
        assertThat(adminRole.hasPermission(readPerm)).isFalse();
    }

    @Test
    @DisplayName("Should find roles containing specific permission")
    void shouldFindRolesByPermission() {
        Permission writePerm = new Permission("WRITE", "DB", "desc");
        adminRole.addPermission(writePerm);

        roleManager.add(adminRole);
        roleManager.add(userRole);

        List<Role> roles = roleManager.findRolesWithPermission("WRITE", "DB");

        assertThat(roles).hasSize(1).contains(adminRole);
    }

    @Test
    @DisplayName("Should sort roles by permission count using RoleSorters")
    void shouldSortByPermissionCount() {
        adminRole.addPermission(new Permission("P1", "R1", "desc"));
        adminRole.addPermission(new Permission("P2", "R2", "desc"));
        userRole.addPermission(new Permission("P3", "R3", "desc"));

        roleManager.add(adminRole);
        roleManager.add(userRole);

        List<Role> sorted = roleManager.findAll(_ -> true, RoleSorters.byPermissionCount());

        assertThat(sorted).containsExactly(userRole, adminRole);
    }

    @Test
    @DisplayName("Should return a copy of the base collection")
    void shouldReturnCollectionCopy() {
        roleManager.add(adminRole);
        roleManager.add(userRole);

        List<Role> all = roleManager.findAll();
        all.clear();

        assertThat(roleManager.count()).isEqualTo(2);
    }
}
