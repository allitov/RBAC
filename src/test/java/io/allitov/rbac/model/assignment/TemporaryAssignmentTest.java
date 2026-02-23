package io.allitov.rbac.model.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TemporaryAssignmentTest {

    @Mock
    private User mockUser;

    @Mock
    private Role mockRole;

    @Mock
    private AssignmentMetadata mockMetadata;

    @Test
    @DisplayName("Should be active when expiration date is in the future")
    void shouldBeActiveInFuture() {
        String futureTime = LocalDateTime.now().plusDays(1).toString();
        TemporaryAssignment assignment = new TemporaryAssignment(mockUser, mockRole, mockMetadata, futureTime, false);

        assertThat(assignment.isActive()).isTrue();
        assertThat(assignment.isExpired()).isFalse();
    }

    @Test
    @DisplayName("Should be inactive and expired when expiration date is in the past")
    void shouldBeExpiredInPast() {
        String pastTime = LocalDateTime.now().minusMinutes(1).toString();
        TemporaryAssignment assignment = new TemporaryAssignment(mockUser, mockRole, mockMetadata, pastTime, false);

        assertThat(assignment.isActive()).isFalse();
        assertThat(assignment.isExpired()).isTrue();
    }

    @Test
    @DisplayName("extend() should update the expiration date and change status")
    void shouldExtendExpiration() {
        String pastTime = LocalDateTime.now().minusMinutes(1).toString();
        TemporaryAssignment assignment = new TemporaryAssignment(mockUser, mockRole, mockMetadata, pastTime, false);

        assertThat(assignment.isActive()).isFalse();

        String futureTime = LocalDateTime.now().plusHours(2).toString();
        assignment.extend(futureTime);

        assertThat(assignment.isActive()).isTrue();
        assertThat(assignment.summary()).contains(futureTime);
    }

    @Test
    @DisplayName("getTimeRemaining() should return positive seconds for future dates")
    void shouldReturnTimeRemaining() {
        String futureTime = LocalDateTime.now().plusSeconds(100).toString();
        TemporaryAssignment assignment = new TemporaryAssignment(mockUser, mockRole, mockMetadata, futureTime, false);

        String remaining = assignment.getTimeRemaining();

        assertThat(remaining).contains("Seconds left:");
        long seconds = Long.parseLong(remaining.split(": ")[1]);
        assertThat(seconds).isBetween(95L, 100L);
    }

    @Test
    @DisplayName("summary() should include superclass info and expiration date")
    void summaryShouldIncludeExtraInfo() {
        String expires = "2026-12-31T23:59:59";
        TemporaryAssignment assignment = new TemporaryAssignment(mockUser, mockRole, mockMetadata, expires, true);

        assertThat(assignment.summary()).contains("Expires at: " + expires);
    }

    @Test
    @DisplayName("assignmentType should be TEMPORARY")
    void checkType() {
        TemporaryAssignment assignment =
                new TemporaryAssignment(mockUser, mockRole, mockMetadata, "2030-01-01T00:00:00", false);

        assertThat(assignment.assignmentType()).isEqualTo("TEMPORARY");
    }
}
