package io.allitov.rbac.model.assignment;

import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;

public interface RoleAssignment {

    String assignmentId();

    User user();

    Role role();

    AssignmentMetadata metadata();

    boolean isActive();

    String assignmentType();
}
