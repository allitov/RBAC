package io.allitov.rbac.command.impl.assignment;

import io.allitov.rbac.RBACSystem;
import io.allitov.rbac.command.Command;
import io.allitov.rbac.model.assignment.RoleAssignment;
import io.allitov.rbac.model.assignment.TemporaryAssignment;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AssignmentExtendCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        System.out.print("Как найти назначение? (1 - по ID, 2 - по username + role): ");
        String choice = scanner.nextLine();

        RoleAssignment target = null;

        if ("1".equals(choice)) {
            System.out.print("Введите Assignment ID: ");
            String id = scanner.nextLine();
            try {
                target = system.getAssignmentManager().findById(id);
            } catch (Exception e) {
                System.out.println("Назначение с таким ID не найдено.");
                return;
            }
        } else {
            System.out.print("Введите username: ");
            String username = scanner.nextLine();
            System.out.print("Введите название роли: ");
            String roleName = scanner.nextLine();

            target = system.getAssignmentManager().findAll().stream()
                    .filter(a -> a.user().getUsername().equals(username)
                            && a.role().getName().equals(roleName))
                    .findFirst()
                    .orElse(null);
        }

        if (!(target instanceof TemporaryAssignment tempAssignment)) {
            System.out.println("Ошибка: Назначение не найдено или не является временным.");
            return;
        }

        System.out.print("Текущая дата истечения: " + tempAssignment.getExpiresAt());
        System.out.print("\nВведите новую дату истечения (гггг-мм-ддТчч:мм): ");
        LocalDateTime newExpiry = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        system.getAssignmentManager().extendTemporaryAssignment(tempAssignment.assignmentId(), newExpiry.toString());
        System.out.println("Назначение успешно продлено до " + newExpiry);
    }
}
