package io.allitov.rbac.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;

import io.allitov.rbac.model.assignment.AssignmentMetadata;
import io.allitov.rbac.model.assignment.PermanentAssignment;
import io.allitov.rbac.model.assignment.RoleAssignment;
import io.allitov.rbac.model.assignment.TemporaryAssignment;
import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AssignmentRepositoryTest {

    private AssignmentRepository repository;
    private User testUser;
    private Role testRole;
    private AssignmentMetadata metadata;

    @BeforeEach
    void setUp() {
        repository = new AssignmentRepository();
        repository.clear();

        testUser = User.of("ivanov", "Ivan Ivanov", "ivan@test.com");
        testRole = Role.of("OPERATOR", "Operator role");
        metadata = AssignmentMetadata.now("admin_root", "Standard assignment");
    }

    @Nested
    @DisplayName("CRUD операции")
    class BasicCrudTests {

        @Test
        @DisplayName("Должен успешно добавлять и находить назначение")
        void shouldAddAndFindAssignment() {
            RoleAssignment assignment = new PermanentAssignment(testUser, testRole, metadata);
            repository.add(assignment);

            Optional<RoleAssignment> found = repository.findById(assignment.assignmentId());

            assertThat(found).isPresent().contains(assignment);
            assertThat(repository.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("Должен обновлять существующее назначение")
        void shouldUpdateAssignment() {
            PermanentAssignment assignment = new PermanentAssignment(testUser, testRole, metadata);
            repository.add(assignment);

            assignment.revoke();
            repository.update(assignment);

            RoleAssignment updated =
                    repository.findById(assignment.assignmentId()).get();
            assertThat(updated.isActive()).isFalse();
        }

        @Test
        @DisplayName("Удаление должно возвращать true/false в зависимости от результата")
        void shouldRemoveAssignment() {
            RoleAssignment assignment = new PermanentAssignment(testUser, testRole, metadata);
            repository.add(assignment);

            assertThat(repository.remove(assignment.assignmentId())).isTrue();
            assertThat(repository.remove("non-existent-id")).isFalse();
        }
    }

    @Nested
    @DisplayName("Поиск и фильтрация")
    class FilterTests {

        @Test
        @DisplayName("Должен находить только активные назначения")
        void shouldFindAllActiveAssignments() {
            PermanentAssignment permanent = new PermanentAssignment(testUser, testRole, metadata);

            TemporaryAssignment expired = new TemporaryAssignment(
                    testUser, testRole, metadata, LocalDateTime.now().minusDays(1));

            TemporaryAssignment activeTemp = new TemporaryAssignment(
                    testUser, testRole, metadata, LocalDateTime.now().plusHours(1));

            repository.add(permanent);
            repository.add(expired);
            repository.add(activeTemp);

            List<RoleAssignment> activeOnes = repository.findAllActiveAssignments();

            assertThat(activeOnes)
                    .hasSize(2)
                    .containsExactlyInAnyOrder(permanent, activeTemp)
                    .doesNotContain(expired);
        }

        @Test
        @DisplayName("Поиск назначений конкретного пользователя")
        void shouldFindByUser() {
            User anotherUser = User.of("petrov", "Petr", "petr@test.com");

            repository.add(new PermanentAssignment(testUser, testRole, metadata));
            repository.add(new PermanentAssignment(anotherUser, testRole, metadata));

            List<RoleAssignment> userAssignments = repository.findByUser(testUser);

            assertThat(userAssignments).hasSize(1);
            assertThat(userAssignments.get(0).user().getUsername()).isEqualTo("ivanov");
        }
    }
}
