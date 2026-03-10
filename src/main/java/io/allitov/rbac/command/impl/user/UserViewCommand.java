package io.allitov.rbac.command.impl.user;

import io.allitov.rbac.command.Command;
import io.allitov.rbac.model.assignment.RoleAssignment;
import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;
import io.allitov.rbac.system.RBACSystem;
import io.allitov.rbac.util.ConsoleUtils;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

public class UserViewCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        String username;
        if (!StringUtils.isBlank(args)) {
            username = args.trim();
        } else {
            username = ConsoleUtils.promptString(scanner, "Введите username пользователя", true);
        }

        try {
            User user = system.getUserManager().findByUsername(username);
            List<RoleAssignment> assignments = system.getAssignmentManager().findByUser(user);

            System.out.println("\n--- Профиль пользователя ---");
            System.out.println("Username:  " + user.getUsername());
            System.out.println("Full Name: " + user.getFullName());
            System.out.println("Email:     " + user.getEmail());
            System.out.println("\nНазначенные роли (" + assignments.size() + "):");

            if (assignments.isEmpty()) {
                System.out.println("  (нет активных назначений)");
            } else {
                for (RoleAssignment assignment : assignments) {
                    Role role = assignment.role();
                    System.out.println("  - " + role.getName() + " (" + assignment.assignmentType() + ")");

                    System.out.println("    Разрешения:");
                    role.getPermissions().forEach(p -> System.out.println("      * " + p.format()));
                }
            }
        } catch (RuntimeException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
