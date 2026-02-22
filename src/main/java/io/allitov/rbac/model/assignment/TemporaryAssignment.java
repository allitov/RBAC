package io.allitov.rbac.model.assignment;

import static io.allitov.rbac.model.assignment.AssignmentType.TEMPORARY;

import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;
import java.time.Duration;
import java.time.LocalDateTime;

@SuppressWarnings("java:S2160")
public class TemporaryAssignment extends AbstractRoleAssignment {

    private String expiresAt;
    private final boolean autoRenew;

    public TemporaryAssignment(User user, Role role, AssignmentMetadata metadata, String expiresAt, boolean autoRenew) {
        super(user, role, metadata);
        this.expiresAt = expiresAt;
        this.autoRenew = autoRenew;
    }

    @Override
    public boolean isActive() {
        return LocalDateTime.now().isBefore(LocalDateTime.parse(expiresAt));
    }

    @Override
    public String assignmentType() {
        return TEMPORARY.getValue();
    }

    public void extend(String newExpirationDate) {
        this.expiresAt = newExpirationDate;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(LocalDateTime.parse(expiresAt));
    }

    public String getTimeRemaining() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expires = LocalDateTime.parse(expiresAt);

        return "Seconds left: " + Duration.between(now, expires).toSeconds();
    }

    @Override
    public String summary() {
        return super.summary() + String.format("%nExpires at: %s", expiresAt);
    }
}
