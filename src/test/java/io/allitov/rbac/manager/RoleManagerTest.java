package io.allitov.rbac.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.allitov.rbac.model.role.Permission;
import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.repository.impl.RoleRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleManagerTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleManager roleManager;

    @Nested
    @DisplayName("Тесты поиска ролей")
    class FindingTests {

        @Test
        @DisplayName("findByName: должен возвращать роль, если она найдена")
        void findByNameShouldReturnRole() {
            Role role = Role.of("ADMIN", "Admin description");
            when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(role));

            Role result = roleManager.findByName("ADMIN");

            assertThat(result).isEqualTo(role);
            verify(roleRepository).findByName("ADMIN");
        }

        @Test
        @DisplayName("findByName: должен выбрасывать NoSuchElementException, если роль не найдена")
        void findByNameShouldThrowExceptionIfNotFound() {
            when(roleRepository.findByName("GUEST")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> roleManager.findByName("GUEST")).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("findById: должен возвращать роль по ID")
        void findByIdShouldReturnRole() {
            Role role = Role.of("USER", "User description");
            String roleId = role.getId();
            when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

            Role result = roleManager.findById(roleId);

            assertThat(result).isEqualTo(role);
        }
    }

    @Nested
    @DisplayName("Тесты управления разрешениями")
    class PermissionManagementTests {

        @Test
        @DisplayName("addPermissionToRole: должен находить роль, добавлять разрешение и обновлять репозиторий")
        void addPermissionToRoleShouldUpdateRole() {
            Role role = Role.of("MANAGER", "Desc");
            Permission permission = Permission.of("READ", "REPORTS", "Can read reports");
            when(roleRepository.findByName("MANAGER")).thenReturn(Optional.of(role));

            roleManager.addPermissionToRole("MANAGER", permission);

            assertThat(role.getPermissions()).contains(permission);
            verify(roleRepository).update(role);
        }

        @Test
        @DisplayName("removePermissionFromRole: должен находить роль, удалять разрешение и обновлять репозиторий")
        void removePermissionFromRoleShouldUpdateRole() {
            Role role = Role.of("MANAGER", "Desc");
            Permission permission = Permission.of("READ", "REPORTS", "Can read reports");
            role.addPermission(permission);

            when(roleRepository.findByName("MANAGER")).thenReturn(Optional.of(role));

            roleManager.removePermissionFromRole("MANAGER", permission);

            assertThat(role.getPermissions()).doesNotContain(permission);
            verify(roleRepository).update(role);
        }
    }

    @Nested
    @DisplayName("Тесты общих операций")
    class GeneralOperationsTests {

        @Test
        @DisplayName("exists: должен возвращать true, если роль найдена по имени")
        void existsShouldReturnTrueWhenFound() {
            when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(mock(Role.class)));

            boolean exists = roleManager.exists("ADMIN");

            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("add: должен делегировать вызов репозиторию")
        void addShouldDelegateToRepository() {
            Role role = Role.of("NEW_ROLE", "Desc");

            roleManager.add(role);

            verify(roleRepository).add(role);
        }

        @Test
        @DisplayName("remove: должен вызывать удаление по ID роли")
        void removeShouldCallRepositoryWithId() {
            Role role = Role.of("TO_DELETE", "Desc");
            String id = role.getId();
            when(roleRepository.remove(id)).thenReturn(true);

            boolean result = roleManager.remove(role);

            assertThat(result).isTrue();
            verify(roleRepository).remove(id);
        }

        @Test
        @DisplayName("findRolesWithPermission: должен делегировать поиск по атрибутам репозиторию")
        void findRolesWithPermissionShouldCallRepository() {
            List<Role> roles = List.of(mock(Role.class));
            when(roleRepository.findAllRolesWithPermissionAndResource("READ", "DATA"))
                    .thenReturn(roles);

            List<Role> result = roleManager.findRolesWithPermission("READ", "DATA");

            assertThat(result).isEqualTo(roles);
        }
    }
}
