package io.allitov.rbac.filter.assignment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import io.allitov.rbac.model.assignment.AssignmentMetadata;
import io.allitov.rbac.model.assignment.RoleAssignment;
import io.allitov.rbac.model.assignment.TemporaryAssignment;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssignmentFiltersTest {

    @Mock
    private RoleAssignment assignment;

    @Mock
    private AssignmentMetadata metadata;

    @Test
    @DisplayName("Фильтр assignedAfter должен проверять дату создания")
    void shouldFilterByAssignmentDate() {
        LocalDateTime assignmentDate = LocalDateTime.of(2026, 3, 1, 10, 0);
        when(assignment.metadata()).thenReturn(metadata);
        when(metadata.getAssignedAt()).thenReturn(assignmentDate);

        AssignmentFilter filter = AssignmentFilters.assignedAfter("2026-02-28T00:00:00");
        assertThat(filter.test(assignment)).isTrue();

        AssignmentFilter filterFalse = AssignmentFilters.assignedAfter("2026-03-02T00:00:00");
        assertThat(filterFalse.test(assignment)).isFalse();
    }

    @Test
    @DisplayName("Фильтр expiringBefore должен работать только для TemporaryAssignment")
    void shouldFilterByExpirationDate() {
        LocalDateTime expiry = LocalDateTime.of(2026, 3, 10, 10, 0);

        TemporaryAssignment tempAssignment = org.mockito.Mockito.mock(TemporaryAssignment.class);
        when(tempAssignment.getExpiresAt()).thenReturn(expiry);

        AssignmentFilter filter = AssignmentFilters.expiringBefore("2026-03-11T00:00:00");

        assertThat(filter.test(tempAssignment)).isTrue();
    }
}
