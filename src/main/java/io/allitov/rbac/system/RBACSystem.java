package io.allitov.rbac.system;

import io.allitov.rbac.manager.AssignmentManager;
import io.allitov.rbac.manager.RoleManager;
import io.allitov.rbac.manager.UserManager;
import io.allitov.rbac.model.assignment.AssignmentMetadata;
import io.allitov.rbac.model.assignment.PermanentAssignment;
import io.allitov.rbac.model.role.Permission;
import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;
import io.allitov.rbac.repository.impl.AssignmentRepository;
import io.allitov.rbac.repository.impl.RoleRepository;
import io.allitov.rbac.repository.impl.UserRepository;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RBACSystem {

    private final UserManager userManager;
    private final RoleManager roleManager;
    private final AssignmentManager assignmentManager;

    private String currentUser;

    public RBACSystem() {
        this.userManager = new UserManager(new UserRepository());
        this.roleManager = new RoleManager(new RoleRepository());
        this.assignmentManager = new AssignmentManager(new AssignmentRepository(), userManager, roleManager);
    }

    public void initialize() {
        var readPermission = Permission.of("READ", "users", "Read user data.");
        var writePermission = Permission.of("WRITE", "users", "Write user data.");
        var deletePermission = Permission.of("DELETE", "users", "Delete user data.");

        var adminRole = Role.of("Admin", "System Administrator");
        adminRole.addPermission(writePermission);
        adminRole.addPermission(readPermission);
        adminRole.addPermission(deletePermission);

        var managerRole = Role.of("Manager", "System Manager");
        managerRole.addPermission(readPermission);
        managerRole.addPermission(writePermission);

        var viewerRole = Role.of("Viewer", "System Viewer");
        viewerRole.addPermission(readPermission);

        roleManager.add(adminRole);
        roleManager.add(managerRole);
        roleManager.add(viewerRole);

        var adminUser = User.of("Admin", "System Administrator", "admin@mail.com");

        userManager.add(adminUser);

        var adminAssignment =
                new PermanentAssignment(adminUser, adminRole, AssignmentMetadata.now("Super Admin", "Now you rule!"));
        assignmentManager.add(adminAssignment);
    }

    public String generateStatistics() {
        return "RBAC System statistics%nUsers count: %d%nRoles count: %d%nAssignments count: %d%n"
                .formatted(userManager.count(), roleManager.count(), assignmentManager.count());
    }
}
