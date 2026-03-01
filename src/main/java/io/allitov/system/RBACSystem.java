package io.allitov.system;

import io.allitov.rbac.model.assignment.AssignmentMetadata;
import io.allitov.rbac.model.assignment.PermanentAssignment;
import io.allitov.rbac.model.role.Permission;
import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;
import io.allitov.rbac.repository.assignment.AssignmentManager;
import io.allitov.rbac.repository.role.RoleManager;
import io.allitov.rbac.repository.user.UserManager;

public class RBACSystem {

    private final UserManager userManager;
    private final RoleManager roleManager;
    private final AssignmentManager assignmentManager;
    private String currentUser;

    public RBACSystem() {
        this.userManager = new UserManager();
        this.roleManager = new RoleManager();
        this.assignmentManager = new AssignmentManager(userManager, roleManager);
    }

    public void initialize() {
        var readPermission = new Permission("READ", "users", "Read user data.");
        var writePermission = new Permission("WRITE", "users", "Write user data.");
        var deletePermission = new Permission("DELETE", "users", "Delete user data.");

        var adminRole = new Role("Admin", "System Administrator");
        adminRole.addPermission(writePermission);
        adminRole.addPermission(readPermission);
        adminRole.addPermission(deletePermission);

        var managerRole = new Role("Manager", "System Manager");
        managerRole.addPermission(readPermission);
        managerRole.addPermission(writePermission);

        var viewerRole = new Role("Viewer", "System Viewer");
        viewerRole.addPermission(readPermission);

        roleManager.add(adminRole);
        roleManager.add(managerRole);
        roleManager.add(viewerRole);

        var adminUser = new User("Admin", "System Administrator", "admin@mail.com");

        userManager.add(adminUser);

        var adminAssignment =
                new PermanentAssignment(adminUser, adminRole, AssignmentMetadata.now("Super Admin", "Now you rule!"));
        assignmentManager.add(adminAssignment);
    }

    public String generateStatistics() {
        return String.format(
                "RBAC System statistics%nUsers count: %d%nRoles count: %d%nAssignments count: %d%n",
                userManager.count(), roleManager.count(), assignmentManager.count());
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public RoleManager getRoleManager() {
        return roleManager;
    }

    public AssignmentManager getAssignmentManager() {
        return assignmentManager;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String username) {
        this.currentUser = username;
    }
}
