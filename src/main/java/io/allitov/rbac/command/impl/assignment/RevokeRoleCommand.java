package io.allitov.rbac.command.impl.assignment;

import io.allitov.rbac.RBACSystem;
import io.allitov.rbac.command.Command;
import io.allitov.rbac.model.assignment.PermanentAssignment;
import io.allitov.rbac.model.assignment.RoleAssignment;
import io.allitov.rbac.model.user.User;
import java.util.List;
import java.util.Scanner;

public class RevokeRoleCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        System.out.print("Введите username пользователя: ");
        String username = scanner.nextLine();
        User user = system.getUserManager().findByUsername(username);
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        List<RoleAssignment> activeAssignments = system.getAssignmentManager().findByUser(user).stream()
                .filter(RoleAssignment::isActive)
                .toList();

        if (activeAssignments.isEmpty()) {
            System.out.println("У пользователя нет активных назначений.");
            return;
        }

        System.out.println("Активные назначения пользователя:");
        for (int i = 0; i < activeAssignments.size(); i++) {
            RoleAssignment ra = activeAssignments.get(i);
            System.out.printf(
                    "%d. [%s] Роль: %s (ID: %s)%n",
                    i + 1, ra.assignmentType(), ra.role().getName(), ra.assignmentId());
        }

        System.out.print("Выберите номер назначения для отзыва: ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;
        RoleAssignment target = activeAssignments.get(choice);

        if (target instanceof PermanentAssignment pa) {
            pa.revoke();
            system.getAssignmentManager().revokeAssignment(target.assignmentId());
            System.out.println("Постоянное назначение успешно отозвано.");
        } else {
            system.getAssignmentManager().remove(target);
            System.out.println("Временное назначение успешно удалено.");
        }
    }
}
