package io.allitov.rbac.command.impl.user;

import io.allitov.rbac.RBACSystem;
import io.allitov.rbac.command.Command;
import io.allitov.rbac.model.assignment.RoleAssignment;
import io.allitov.rbac.model.user.User;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.lang3.Strings;

public class UserDeleteCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        String username;
        if (args != null && !args.isBlank()) {
            username = args.trim();
        } else {
            System.out.print("Введите username пользователя для удаления: ");
            username = scanner.nextLine();
        }

        if (!system.getUserManager().existsByUsername(username)) {
            System.err.println("Ошибка: Пользователь '" + username + "' не найден.");
            return;
        }

        User user = system.getUserManager().findByUsername(username);
        List<RoleAssignment> assignments = system.getAssignmentManager().findByUser(user);

        System.out.println("ВНИМАНИЕ: Будет удален пользователь: " + user.format());
        if (!assignments.isEmpty()) {
            System.out.println("У пользователя есть " + assignments.size() + " назначенных ролей. Они будут удалены.");
        }

        System.out.print("Вы уверены? Введите 'да' для подтверждения: ");
        String confirmation = scanner.nextLine();

        if (!Strings.CI.equals(confirmation.trim(), "да")) {
            System.out.println("Удаление отменено.");
            return;
        }

        try {
            for (RoleAssignment assignment : assignments) {
                system.getAssignmentManager().remove(assignment);
            }
            system.getUserManager().remove(username);
            System.out.println("Успех: Пользователь '" + username + "' и все его назначения удалены.");
        } catch (Exception e) {
            System.err.println("Ошибка при удалении: " + e.getMessage());
        }
    }
}
