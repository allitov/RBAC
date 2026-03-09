package io.allitov.rbac.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.allitov.rbac.model.assignment.AssignmentMetadata;
import io.allitov.rbac.model.assignment.PermanentAssignment;
import io.allitov.rbac.model.assignment.RoleAssignment;
import io.allitov.rbac.model.assignment.TemporaryAssignment;
import io.allitov.rbac.model.role.Permission;
import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;
import io.allitov.rbac.repository.impl.AssignmentRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssignmentManagerTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private UserManager userManager;

    @Mock
    private RoleManager roleManager;

    @InjectMocks
    private AssignmentManager assignmentManager;

    private User testUser;
    private Role testRole;
    private AssignmentMetadata metadata;

    @BeforeEach
    void setUp() {
        testUser = User.of("ivanov", "Ivan", "ivan@test.com");
        testRole = Role.of("ADMIN", "Desc");
        metadata = AssignmentMetadata.now("sys", "reason");
    }

    @Nested
    @DisplayName("Проверка прав и разрешений")
    class SecurityLogicTests {

        @Test
        @DisplayName("userHasPermission: должен возвращать true, если у роли пользователя есть нужное разрешение")
        void userHasPermissionShouldWork() {
            RoleAssignment assignment = new PermanentAssignment(testUser, testRole, metadata);
            when(assignmentRepository.findByUser(testUser)).thenReturn(List.of(assignment));

            testRole.addPermission(Permission.of("READ", "FILES", "desc"));

            boolean hasPermission = assignmentManager.userHasPermission(testUser, "READ", "FILES");

            assertThat(hasPermission).isTrue();
        }

        @Test
        @DisplayName("getUserPermissions: должен собирать уникальный Set всех разрешений пользователя")
        void getUserPermissionsShouldAggregateSet() {
            Permission p1 = Permission.of("P1", "RES", "D");
            testRole.addPermission(p1);
            RoleAssignment a1 = new PermanentAssignment(testUser, testRole, metadata);

            when(assignmentRepository.findByUser(testUser)).thenReturn(List.of(a1));

            Set<Permission> permissions = assignmentManager.getUserPermissions(testUser);

            assertThat(permissions).containsExactly(p1);
        }
    }

    @Nested
    @DisplayName("Специфические операции (Revoke/Extend)")
    class SpecializedOperationsTests {

        @Test
        @DisplayName("revokeAssignment: должен вызывать revoke() только у PermanentAssignment")
        void revokeShouldWorkForPermanent() {
            PermanentAssignment pa = spy(new PermanentAssignment(testUser, testRole, metadata));
            when(assignmentRepository.findById("id1")).thenReturn(Optional.of(pa));

            assignmentManager.revokeAssignment("id1");

            assertThat(pa.isActive()).isFalse();
            verify(pa).revoke();
            verify(assignmentRepository).update(pa);
        }

        @Test
        @DisplayName("extendTemporaryAssignment: должен изменять дату окончания у TemporaryAssignment")
        void extendShouldWorkForTemporary() {
            LocalDateTime now = LocalDateTime.now();
            TemporaryAssignment ta = new TemporaryAssignment(testUser, testRole, metadata, now.plusDays(1));
            when(assignmentRepository.findById("id1")).thenReturn(Optional.of(ta));

            String newDate = now.plusDays(5).toString();
            assignmentManager.extendTemporaryAssignment("id1", newDate);

            assertThat(ta.getExpiresAt()).isEqualTo(LocalDateTime.parse(newDate));
            verify(assignmentRepository).update(ta);
        }
    }

    @Nested
    @DisplayName("Валидация при добавлении")
    class AddValidationTests {

        @Test
        @DisplayName("add: должен выбрасывать ошибку, если пользователь не существует")
        void addShouldThrowIfUserMissing() {
            RoleAssignment assignment = new PermanentAssignment(testUser, testRole, metadata);
            when(userManager.existsByUsername(testUser.getUsername())).thenReturn(false);

            assertThatThrownBy(() -> assignmentManager.add(assignment))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Username ivanov not found");
        }

        @Test
        @DisplayName("add: должен успешно добавлять, если пользователь и роль существуют")
        void addShouldWorkIfAllExists() {
            RoleAssignment assignment = new PermanentAssignment(testUser, testRole, metadata);
            when(userManager.existsByUsername(anyString())).thenReturn(true);
            when(roleManager.exists(anyString())).thenReturn(true);

            assignmentManager.add(assignment);

            verify(assignmentRepository).add(assignment);
        }
    }
}
