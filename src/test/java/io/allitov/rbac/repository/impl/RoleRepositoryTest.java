package io.allitov.rbac.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.allitov.rbac.model.role.Permission;
import io.allitov.rbac.model.role.Role;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RoleRepositoryTest {

    private RoleRepository repository;

    @BeforeEach
    void setUp() {
        repository = new RoleRepository();
        repository.clear();
    }

    @Nested
    @DisplayName("Операции сохранения и поиска")
    class SaveAndFindTests {

        @Test
        @DisplayName("Должен сохранять роль и находить её по ID и имени")
        void shouldSaveAndFindRole() {
            Role admin = Role.of("ADMIN", "Full access");
            repository.add(admin);

            assertThat(repository.findById(admin.getId())).isPresent().contains(admin);

            assertThat(repository.findByName("ADMIN")).isPresent().contains(admin);
        }

        @Test
        @DisplayName("Должен выбрасывать исключение при дублировании ID")
        void shouldFailOnDuplicateId() {
            Role role = Role.of("USER", "Desc");
            repository.add(role);

            assertThatThrownBy(() -> repository.add(role))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Duplicate role");
        }
    }

    @Nested
    @DisplayName("Удаление и обновление")
    class UpdateDeleteTests {

        @Test
        @DisplayName("Удаление должно очищать оба индекса")
        void shouldRemoveFromAllIndices() {
            Role role = Role.of("TEMPORARY", "To be deleted");
            repository.add(role);

            boolean removed = repository.remove(role.getId());

            assertThat(removed).isTrue();
            assertThat(repository.findById(role.getId())).isEmpty();
            assertThat(repository.findByName("TEMPORARY")).isEmpty();
        }
    }

    @Nested
    @DisplayName("Сложные фильтры")
    class FilterTests {

        @Test
        @DisplayName("Должен находить роли по разрешению и ресурсу")
        void shouldFindRolesByPermissionAndResource() {
            Permission readSystem = Permission.of("READ", "SYSTEM", "Read sys");
            Permission writeSystem = Permission.of("WRITE", "SYSTEM", "Write sys");

            Role admin = Role.of("ADMIN", "Admin role");
            admin.addPermission(readSystem);
            admin.addPermission(writeSystem);

            Role guest = Role.of("GUEST", "Guest role");
            guest.addPermission(readSystem);

            repository.add(admin);
            repository.add(guest);

            List<Role> writers = repository.findAllRolesWithPermissionAndResource("WRITE", "SYSTEM");

            assertThat(writers).hasSize(1);
            assertThat(writers).extracting(Role::getName).containsExactly("ADMIN");
        }

        @Test
        @DisplayName("Должен сортировать роли по кастомному компаратору")
        void shouldSortRoles() {
            repository.add(Role.of("C_ROLE", "DESCR"));
            repository.add(Role.of("A_ROLE", "DESCR"));
            repository.add(Role.of("B_ROLE", "DESCR"));

            List<Role> sorted = repository.findAll(_ -> true, Comparator.comparing(Role::getName));

            assertThat(sorted).extracting(Role::getName).containsExactly("A_ROLE", "B_ROLE", "C_ROLE");
        }
    }
}
