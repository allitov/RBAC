package io.allitov.rbac.repository.assignment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import io.allitov.rbac.model.assignment.AssignmentMetadata;
import io.allitov.rbac.model.assignment.PermanentAssignment;
import io.allitov.rbac.model.assignment.RoleAssignment;
import io.allitov.rbac.model.assignment.TemporaryAssignment;
import io.allitov.rbac.model.role.Permission;
import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;
import io.allitov.rbac.repository.role.RoleManager;
import io.allitov.rbac.repository.user.UserManager;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssignmentManagerTest {

    @Mock
    private UserManager userManager;

    @Mock
    private RoleManager roleManager;

    @InjectMocks
    private AssignmentManager assignmentManager;

    private User alice;
    private Role adminRole;
    private Role userRole;
    private AssignmentMetadata metadata;

    @BeforeEach
    void setUp() {
        assignmentManager.clear();

        alice = new User("alice", "Alice Smith", "alice@test.com");
        adminRole = new Role("ADMIN", "Admin description");
        userRole = new Role("USER", "User description");
        metadata = AssignmentMetadata.now("tester", "unit-test");

        lenient().when(userManager.exists(anyString())).thenReturn(true);
        lenient().when(roleManager.exists(anyString())).thenReturn(true);
    }

    @Test
    @DisplayName("findByUser: should return assignments only for specific user")
    void shouldFindByUser() {
        User bob = new User("bob", "Bob", "bob@test.com");
        RoleAssignment a1 = new PermanentAssignment(alice, adminRole, metadata);
        RoleAssignment a2 = new PermanentAssignment(bob, userRole, metadata);

        assignmentManager.add(a1);
        assignmentManager.add(a2);

        List<RoleAssignment> result = assignmentManager.findByUser(alice);
        assertThat(result).hasSize(1).contains(a1);
    }

    @Test
    @DisplayName("findByRole: should return assignments for specific role")
    void shouldFindByRole() {
        RoleAssignment a1 = new PermanentAssignment(alice, adminRole, metadata);
        assignmentManager.add(a1);

        assertThat(assignmentManager.findByRole(adminRole)).contains(a1);
        assertThat(assignmentManager.findByRole(userRole)).isEmpty();
    }

    @Test
    @DisplayName("userHasPermission: should check permissions via roles")
    void shouldCheckUserPermission() {
        adminRole.addPermission(new Permission("READ", "FILES", "desc"));
        assignmentManager.add(new PermanentAssignment(alice, adminRole, metadata));

        assertThat(assignmentManager.userHasPermission(alice, "READ", "FILES")).isTrue();
        assertThat(assignmentManager.userHasPermission(alice, "WRITE", "FILES")).isFalse();
    }

    @Test
    @DisplayName("getUserPermissions: should collect all permissions into a set")
    void shouldGetAggregatedPermissions() {
        Permission p1 = new Permission("READ", "FILES", "desc");
        Permission p2 = new Permission("WRITE", "FILES", "desc");
        adminRole.addPermission(p1);
        userRole.addPermission(p2);

        assignmentManager.add(new PermanentAssignment(alice, adminRole, metadata));
        assignmentManager.add(new PermanentAssignment(alice, userRole, metadata));

        Set<Permission> perms = assignmentManager.getUserPermissions(alice);
        assertThat(perms).containsExactlyInAnyOrder(p1, p2);
    }

    @Test
    @DisplayName("getExpiredAssignments: should return only expired temporary assignments")
    void shouldIdentifyExpiredAssignments() {
        String past = LocalDateTime.now().minusDays(1).toString();
        String future = LocalDateTime.now().plusDays(1).toString();

        TemporaryAssignment expired = new TemporaryAssignment(alice, adminRole, metadata, past, false);
        TemporaryAssignment active = new TemporaryAssignment(alice, userRole, metadata, future, false);

        assignmentManager.add(expired);
        assignmentManager.add(active);

        assertThat(assignmentManager.getExpiredAssignments()).containsExactly(expired);
        assertThat(assignmentManager.getActiveAssignments()).containsExactly(active);
    }

    @Test
    @DisplayName("revokeAssignment: should deactivate permanent assignment")
    void shouldRevokePermanent() {
        PermanentAssignment pa = new PermanentAssignment(alice, adminRole, metadata);
        assignmentManager.add(pa);

        assignmentManager.revokeAssignment(pa.assignmentId());
        assertThat(pa.isActive()).isFalse();
    }

    @Test
    @DisplayName("extendTemporaryAssignment: should update expiry date")
    void shouldExtendTemporary() {
        String initial = LocalDateTime.now().plusDays(1).toString();
        TemporaryAssignment ta = new TemporaryAssignment(alice, adminRole, metadata, initial, false);
        assignmentManager.add(ta);

        String nextWeek = LocalDateTime.now().plusWeeks(1).toString();
        assignmentManager.extendTemporaryAssignment(ta.assignmentId(), nextWeek);

        assertThat(ta.getExpiresAt()).isEqualTo(nextWeek);
    }

    @Test
    @DisplayName("add: should throw if user or role does not exist")
    void shouldValidateBeforeAdd() {
        when(userManager.exists("alice")).thenReturn(false);
        RoleAssignment a = new PermanentAssignment(alice, adminRole, metadata);

        assertThatThrownBy(() -> assignmentManager.add(a)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("remove: should delete assignment from map")
    void shouldRemoveAssignment() {
        RoleAssignment a = new PermanentAssignment(alice, adminRole, metadata);
        assignmentManager.add(a);

        boolean removed = assignmentManager.remove(a);
        assertThat(removed).isTrue();
        assertThat(assignmentManager.count()).isZero();
    }

    @Test
    @DisplayName("findAll with filter and sorter: should return processed list")
    void shouldFilterAndSort() {
        RoleAssignment a1 = new PermanentAssignment(alice, adminRole, metadata);
        RoleAssignment a2 = new PermanentAssignment(alice, userRole, metadata);
        assignmentManager.add(a1);
        assignmentManager.add(a2);

        List<RoleAssignment> result = assignmentManager.findAll(
                _ -> true, Comparator.comparing(a -> a.role().getName()));

        assertThat(result).containsExactly(a1, a2);
    }
}
