package io.allitov.rbac.model.role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleTest {

    private Role role;
    private Permission mockPermission;

    @BeforeEach
    void setUp() {
        role = new Role("ADMIN", "System Administrator");
        mockPermission = mock(Permission.class);
    }

    @Test
    @DisplayName("Should initialize with generated ID and empty permissions")
    void shouldInitializeCorrectly() {
        assertThat(role.getId()).startsWith("role_");
        assertThat(role.getName()).isEqualTo("ADMIN");
        assertThat(role.getPermissions()).isEmpty();
    }

    @Test
    @DisplayName("Should add and remove permissions")
    void shouldManagePermissions() {
        role.addPermission(mockPermission);
        assertThat(role.getPermissions()).hasSize(1).contains(mockPermission);

        role.removePermission(mockPermission);
        assertThat(role.getPermissions()).isEmpty();
    }

    @Test
    @DisplayName("hasPermission(Permission) should work correctly")
    void shouldCheckPermissionObject() {
        role.addPermission(mockPermission);
        assertThat(role.hasPermission(mockPermission)).isTrue();
    }

    @Test
    @DisplayName("hasPermission(String, String) should use Permission.matches logic")
    void shouldCheckPermissionByPattern() {
        String namePart = "READ";
        String resPart = "user";

        when(mockPermission.matches(namePart, resPart)).thenReturn(true);
        role.addPermission(mockPermission);

        assertThat(role.hasPermission(namePart, resPart)).isTrue();
        assertThat(role.hasPermission("WRITE", "admin")).isFalse();
    }

    @Test
    @DisplayName("getPermissions() should return unmodifiable set")
    void shouldReturnUnmodifiableSet() {
        Set<Permission> permissions = role.getPermissions();
        assertThatThrownBy(() -> permissions.add(mockPermission)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("equals and hashCode should be based only on ID")
    void equalsAndHashCodeShouldBeBasedOnId() {
        Role anotherRole = new Role("ADMIN", "System Administrator");

        assertThat(role).isNotEqualTo(anotherRole).doesNotHaveSameHashCodeAs(anotherRole);
    }

    @Test
    @DisplayName("format() should contain role details and formatted permissions")
    void formatShouldReturnFullInfo() {
        when(mockPermission.format()).thenReturn("FORMATTED_PERMISSION");
        role.addPermission(mockPermission);

        String result = role.format();

        assertThat(result)
                .contains("Role: ADMIN")
                .contains("ID: " + role.getId())
                .contains("Description: System Administrator")
                .contains("Permissions: (1)")
                .contains("- FORMATTED_PERMISSION");
    }
}
