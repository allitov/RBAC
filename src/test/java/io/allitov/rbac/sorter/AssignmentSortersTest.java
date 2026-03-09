package io.allitov.rbac.sorter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.allitov.rbac.model.assignment.AssignmentMetadata;
import io.allitov.rbac.model.assignment.RoleAssignment;
import io.allitov.rbac.model.user.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssignmentSortersTest {

    @Mock
    private RoleAssignment a1, a2, a3;

    @Test
    @DisplayName("Должен сортировать назначения по username пользователя")
    void shouldSortByUsername() {
        setupUser(a1, "alice");
        setupUser(a2, "bob");
        setupUser(a3, "charlie");
        List<RoleAssignment> assignments = new ArrayList<>(List.of(a3, a1, a2));

        assignments.sort(AssignmentSorters.byUsername());

        assertThat(assignments).containsExactly(a1, a2, a3);
    }

    @Test
    @DisplayName("Должен сортировать назначения по дате создания")
    void shouldSortByAssignmentDate() {
        setupDate(a1, LocalDateTime.now().minusDays(2));
        setupDate(a2, LocalDateTime.now().minusDays(1));
        setupDate(a3, LocalDateTime.now());
        List<RoleAssignment> assignments = new ArrayList<>(List.of(a3, a1, a2));

        assignments.sort(AssignmentSorters.byAssignmentDate());

        assertThat(assignments).containsExactly(a1, a2, a3);
    }

    private void setupUser(RoleAssignment assignment, String username) {
        User user = User.of(username, "Name", "email@test.com");
        when(assignment.user()).thenReturn(user);
    }

    private void setupDate(RoleAssignment assignment, LocalDateTime date) {
        AssignmentMetadata metaMock = mock(AssignmentMetadata.class);
        when(metaMock.getAssignedAt()).thenReturn(date);
        when(assignment.metadata()).thenReturn(metaMock);
    }
}
