package io.allitov.rbac.model.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PermanentAssignmentTest {

    @Mock
    private User mockUser;

    @Mock
    private Role mockRole;

    @Mock
    private AssignmentMetadata mockMetadata;

    @InjectMocks
    private PermanentAssignment assignment;

    @Test
    @DisplayName("Should have PERMANENT assignment type")
    void shouldReturnCorrectType() {
        assertThat(assignment.assignmentType()).isEqualTo("PERMANENT");
    }

    @Test
    @DisplayName("Should be active by default")
    void shouldBeActiveByDefault() {
        assertThat(assignment.isRevoked()).isFalse();
        assertThat(assignment.isActive()).isTrue();
    }

    @Test
    @DisplayName("Should become inactive when revoked")
    void shouldChangeStatusWhenRevoked() {
        assignment.revoke();

        assertThat(assignment.isRevoked()).isTrue();
        assertThat(assignment.isActive()).isFalse();
    }

    @Test
    @DisplayName("Should correctly pass components to superclass")
    void shouldStoreConstructorArguments() {
        assertThat(assignment.user()).isEqualTo(mockUser);
        assertThat(assignment.role()).isEqualTo(mockRole);
        assertThat(assignment.metadata()).isEqualTo(mockMetadata);
    }

    @Test
    @DisplayName("Multiple revoke calls should keep the state as revoked")
    void multipleRevokesShouldNotChangeState() {
        assignment.revoke();
        assignment.revoke();

        assertThat(assignment.isRevoked()).isTrue();
        assertThat(assignment.isActive()).isFalse();
    }
}
