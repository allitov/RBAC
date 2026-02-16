package io.allitov.rbac.model;

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
        return "PERMANENT";
    }

    public void revoke() {
        revoked = true;
    }

    public boolean isRevoked() {
        return revoked;
    }
}
