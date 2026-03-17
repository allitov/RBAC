package io.allitov.rbac.command.impl.role;

import io.allitov.rbac.RBACSystem;
import io.allitov.rbac.command.Command;
import io.allitov.rbac.model.role.Permission;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class RoleAddPermissionCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        String roleName;

        if (args.isBlank()) {
            System.out.print("Введите название роли, которой нужно добавить право: ");
            roleName = scanner.nextLine().trim();
        } else {
            roleName = args.trim();
        }

        if (roleName.isEmpty()) {
            System.out.println("Название роли не может быть пустым.");
            return;
        }

        try {
            if (!system.getRoleManager().exists(roleName)) {
                System.out.println("Ошибка: Роль '" + roleName + "' не найдена.");
                return;
            }

            System.out.println("--- Введите данные нового права ---");

            System.out.print("Название права (например, CREATE): ");
            String pName = scanner.nextLine().trim();

            System.out.print("Ресурс (например, reports): ");
            String pResource = scanner.nextLine().trim();

            System.out.print("Описание права: ");
            String pDescription = scanner.nextLine().trim();

            Permission permission = Permission.of(pName, pResource, pDescription);

            system.getRoleManager().addPermissionToRole(roleName, permission);

            System.out.println("Право успешно добавлено к роли '" + roleName + "'.");

        } catch (NoSuchElementException e) {
            System.out.println("Ошибка: Роль '" + roleName + "' не найдена.");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении права: " + e.getMessage());
        }
    }
}
