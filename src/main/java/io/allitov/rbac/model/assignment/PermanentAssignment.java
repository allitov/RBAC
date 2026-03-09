package io.allitov.rbac.model.assignment;

import static io.allitov.rbac.model.assignment.AssignmentType.PERMANENT;

import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
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
}
