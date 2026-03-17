package io.allitov.rbac.command.impl.role;

import io.allitov.rbac.RBACSystem;
import io.allitov.rbac.command.Command;
import io.allitov.rbac.model.role.Role;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class RoleViewCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        String roleName;

        if (args.isBlank()) {
            System.out.print("Введите название роли для просмотра: ");
            roleName = scanner.nextLine().trim();
        } else {
            roleName = args.trim();
        }

        if (roleName.isEmpty()) {
            System.out.println("Название роли не может быть пустым.");
            return;
        }

        try {
            Role role = system.getRoleManager().findByName(roleName);

            System.out.println("\n--- Детальная информация о роли ---");
            System.out.print(role.format());
            System.out.println("----------------------------------");

        } catch (NoSuchElementException e) {
            System.out.println("Ошибка: Роль с названием '" + roleName + "' не найдена.");
        }
    }
}
