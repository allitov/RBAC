package io.allitov.rbac.model.assignment;

import static io.allitov.rbac.model.assignment.AssignmentType.TEMPORARY;

import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class TemporaryAssignment extends AbstractRoleAssignment {

    private LocalDateTime expiresAt;

    public TemporaryAssignment(User user, Role role, AssignmentMetadata metadata, LocalDateTime expiresAt) {
        super(user, role, metadata);
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean isActive() {
        return LocalDateTime.now().isBefore(expiresAt);
    }

    @Override
    public String assignmentType() {
        return TEMPORARY.getValue();
    }

    public void extend(LocalDateTime newExpirationDate) {
        this.expiresAt = newExpirationDate;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public String getTimeRemaining() {
        LocalDateTime now = LocalDateTime.now();
        return "Seconds left: " + Duration.between(now, expiresAt).toSeconds();
    }

    @Override
    public String summary() {
        return super.summary() + "%nExpires at: %s".formatted(expiresAt);
    }
}
