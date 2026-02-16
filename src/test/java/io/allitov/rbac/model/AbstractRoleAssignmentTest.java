package io.allitov.rbac.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AbstractRoleAssignmentTest {

    private User mockUser;
    private Role mockRole;
    private AssignmentMetadata mockMetadata;

    private static class TestRoleAssignment extends AbstractRoleAssignment {

        private final boolean active;
        private final String type;

        public TestRoleAssignment(User user, Role role, AssignmentMetadata metadata, boolean active, String type) {
            super(user, role, metadata);
            this.active = active;
            this.type = type;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        @Override
        public String assignmentType() {
            return type;
        }
    }

    @BeforeEach
    void setUp() {
        mockUser = mock(User.class);
        mockRole = mock(Role.class);
        mockMetadata = mock(AssignmentMetadata.class);
    }

    @Test
    @DisplayName("Should correctly initialize common fields and generate unique ID")
    void shouldInitializeCommonFields() {
        AbstractRoleAssignment assignment = new TestRoleAssignment(mockUser, mockRole, mockMetadata, true, "PERMANENT");

        assertThat(assignment.assignmentId()).isNotNull().isNotBlank();
        assertThat(assignment.user()).isEqualTo(mockUser);
        assertThat(assignment.role()).isEqualTo(mockRole);
        assertThat(assignment.metadata()).isEqualTo(mockMetadata);
    }

    @Test
    @DisplayName("summary() should format string using data from dependencies")
    void summaryShouldReturnFormattedString() {
        when(mockRole.getName()).thenReturn("EDITOR");
        when(mockUser.username()).thenReturn("john_doe");
        when(mockMetadata.assignedBy()).thenReturn("admin_1");
        when(mockMetadata.assignedAt()).thenReturn("2026-02-16");
        when(mockMetadata.reason()).thenReturn("Project access");

        AbstractRoleAssignment assignment = new TestRoleAssignment(mockUser, mockRole, mockMetadata, true, "TEMPORARY");

        String summary = assignment.summary();

        assertThat(summary)
                .contains("[TEMPORARY]")
                .contains("EDITOR assigned to john_doe")
                .contains("by admin_1 at 2026-02-16")
                .contains("Reason: Project access")
                .contains("Status: ACTIVE");
    }

    @Test
    @DisplayName("summary() should show REVOKED status when isActive is false")
    void summaryShouldShowRevokedStatus() {
        when(mockRole.getName()).thenReturn("GUEST");
        when(mockUser.username()).thenReturn("guest_user");
        when(mockMetadata.assignedBy()).thenReturn("system");
        when(mockMetadata.assignedAt()).thenReturn("now");
        when(mockMetadata.reason()).thenReturn("Expired");

        AbstractRoleAssignment assignment =
                new TestRoleAssignment(mockUser, mockRole, mockMetadata, false, "PERMANENT");

        assertThat(assignment.summary()).contains("Status: REVOKED");
    }

    @Test
    @DisplayName("equals and hashCode should be based strictly on assignmentId")
    void equalsAndHashCodeShouldWork() {
        AbstractRoleAssignment assignment1 = new TestRoleAssignment(mockUser, mockRole, mockMetadata, true, "TYPE");
        AbstractRoleAssignment assignment2 = new TestRoleAssignment(mockUser, mockRole, mockMetadata, true, "TYPE");

        assertThat(assignment1).doesNotHaveSameHashCodeAs(assignment2).isNotEqualTo(assignment2);
    }
}
