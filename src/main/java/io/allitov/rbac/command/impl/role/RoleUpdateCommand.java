package io.allitov.rbac.command.impl.role;

import io.allitov.rbac.RBACSystem;
import io.allitov.rbac.command.Command;
import io.allitov.rbac.model.role.Role;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class RoleUpdateCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        String roleName;

        if (args.isBlank()) {
            System.out.print("Введите название роли для обновления: ");
            roleName = scanner.nextLine().trim();
        } else {
            roleName = args.trim();
        }

        try {
            Role role = system.getRoleManager().findByName(roleName);

            System.out.println("Редактирование роли: " + role.getName());

            System.out.print("Новое название [" + role.getName() + "]: ");
            String newName = scanner.nextLine().trim();

            System.out.print("Новое описание [" + role.getDescription() + "]: ");
            String newDescription = scanner.nextLine().trim();
            system.getRoleManager().update(newName, newDescription);

            System.out.println("Данные роли успешно обновлены.");

        } catch (NoSuchElementException e) {
            System.out.println("Ошибка: Роль '" + roleName + "' не найдена.");
        } catch (Exception e) {
            System.out.println("Ошибка при обновлении: " + e.getMessage());
        }
    }
}
