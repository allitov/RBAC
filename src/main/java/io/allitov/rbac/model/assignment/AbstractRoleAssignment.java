package io.allitov.rbac.model.assignment;

import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractRoleAssignment implements RoleAssignment {

    @EqualsAndHashCode.Include
    private final String assignmentId = UUID.randomUUID().toString();

    private final User user;
    private final Role role;
    private final AssignmentMetadata metadata;

    @Override
    public String assignmentId() {
        return assignmentId;
    }

    @Override
    public User user() {
        return user;
    }

    @Override
    public Role role() {
        return role;
    }

    @Override
    public AssignmentMetadata metadata() {
        return metadata;
    }

    public String summary() {
        return "[%s] %s assigned to %s by %s at %s%nReason: %s%nStatus: %s"
                .formatted(
                        assignmentType(),
                        role.getName(),
                        user.getUsername(),
                        metadata.getAssignedBy(),
                        metadata.getAssignedAt(),
                        metadata.getReason(),
                        isActive() ? "ACTIVE" : "REVOKED");
    }
}
