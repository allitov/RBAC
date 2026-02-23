package io.allitov.rbac.model.assignment;

import static io.allitov.rbac.model.assignment.AssignmentType.PERMANENT;

import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;

@SuppressWarnings("java:S2160")
public class PermanentAssignment extends AbstractRoleAssignment {

    private boolean revoked = false;

    public PermanentAssignment(User user, Role role, AssignmentMetadata metadata) {
        super(user, role, metadata);
    }

    @Override
    public boolean isActive() {
        return !isRevoked();
    }

    @Override
    public String assignmentType() {
        return PERMANENT.getValue();
    }

    public void revoke() {
        revoked = true;
    }

    public boolean isRevoked() {
        return revoked;
    }
}
