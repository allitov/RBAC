package io.allitov.rbac.command.impl.role;

import io.allitov.rbac.RBACSystem;
import io.allitov.rbac.command.Command;
import io.allitov.rbac.model.role.Permission;
import io.allitov.rbac.model.role.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class RoleRemovePermissionCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        String roleName;

        if (args.isBlank()) {
            System.out.print("Введите название роли, у которой нужно удалить право: ");
            roleName = scanner.nextLine().trim();
        } else {
            roleName = args.trim();
        }

        try {
            Role role = system.getRoleManager().findByName(roleName);
            List<Permission> permissions = new ArrayList<>(role.getPermissions());

            if (permissions.isEmpty()) {
                System.out.println("У роли '" + roleName + "' нет назначенных прав.");
                return;
            }

            System.out.println("Список прав роли '" + roleName + "':");
            for (int i = 0; i < permissions.size(); i++) {
                System.out.println((i + 1) + ". " + permissions.get(i).format());
            }

            System.out.print("Введите номер права для удаления (1-" + permissions.size() + "): ");
            String input = scanner.nextLine().trim();

            try {
                int index = Integer.parseInt(input) - 1;

                if (index < 0 || index >= permissions.size()) {
                    System.out.println("Ошибка: Неверный номер.");
                    return;
                }

                Permission toRemove = permissions.get(index);

                system.getRoleManager().removePermissionFromRole(roleName, toRemove);
                System.out.println("Право успешно удалено из роли.");

            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Введите число.");
            }

        } catch (NoSuchElementException e) {
            System.out.println("Ошибка: Роль '" + roleName + "' не найдена.");
        }
    }
}
