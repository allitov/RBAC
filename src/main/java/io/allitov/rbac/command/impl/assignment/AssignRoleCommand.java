package io.allitov.rbac.command.impl.assignment;

import io.allitov.rbac.RBACSystem;
import io.allitov.rbac.command.Command;
import io.allitov.rbac.model.assignment.AssignmentMetadata;
import io.allitov.rbac.model.assignment.PermanentAssignment;
import io.allitov.rbac.model.assignment.RoleAssignment;
import io.allitov.rbac.model.assignment.TemporaryAssignment;
import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AssignRoleCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        System.out.print("Введите username пользователя: ");
        String username = scanner.nextLine();
        User user = system.getUserManager().findByUsername(username);
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        System.out.println("Доступные роли:");
        var roles = system.getRoleManager().findAll();
        for (int i = 0; i < roles.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, roles.get(i).getName());
        }
        System.out.print("Выберите номер роли: ");
        int roleIndex = Integer.parseInt(scanner.nextLine()) - 1;
        Role selectedRole = roles.get(roleIndex);

        System.out.print("Тип назначения (1 - Постоянное, 2 - Временное): ");
        String typeChoice = scanner.nextLine();

        System.out.print("Введите причину назначения: ");
        String reason = scanner.nextLine();

        AssignmentMetadata metadata = AssignmentMetadata.now(system.getCurrentUser(), reason);
        RoleAssignment assignment;

        if ("2".equals(typeChoice)) {
            System.out.print("Введите дату истечения (гггг-мм-ддТчч:мм): ");
            LocalDateTime expiry = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            assignment = new TemporaryAssignment(user, selectedRole, metadata, expiry);
        } else {
            assignment = new PermanentAssignment(user, selectedRole, metadata);
        }

        system.getAssignmentManager().add(assignment);
        System.out.println("Роль успешно назначена!");
    }
}
