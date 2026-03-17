package io.allitov.rbac.command.impl.role;

import io.allitov.rbac.RBACSystem;
import io.allitov.rbac.command.Command;
import io.allitov.rbac.model.role.Permission;
import io.allitov.rbac.model.role.Role;
import java.util.Scanner;

public class RoleCreateCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        System.out.println("=== Создание новой роли ===");

        System.out.print("Введите название роли: ");
        String name = scanner.nextLine();

        System.out.print("Введите описание роли: ");
        String description = scanner.nextLine();

        try {
            Role newRole = Role.of(name, description);
            system.getRoleManager().add(newRole);
            System.out.println("Роль успешно создана (ID: " + newRole.getId() + ").");
            managePermissions(scanner, newRole);
            System.out.println("Настройка роли '" + name + "' завершена.");
        } catch (Exception e) {
            System.out.println("Ошибка при создании роли: " + e.getMessage());
        }
    }

    private void managePermissions(Scanner scanner, Role role) {
        while (true) {
            System.out.print("\nХотите добавить право к этой роли? (y/n): ");
            String answer = scanner.nextLine().trim().toLowerCase();

            if (!answer.equals("y") && !answer.equals("д")) {
                break;
            }

            System.out.print("Введите название права (например, READ): ");
            String pName = scanner.nextLine();

            System.out.print("Введите ресурс (например, user_data): ");
            String pResource = scanner.nextLine();

            System.out.print("Введите описание права: ");
            String pDescription = scanner.nextLine();

            try {
                Permission permission = Permission.of(pName, pResource, pDescription);
                role.addPermission(permission);
                System.out.println("Право добавлено.");
            } catch (Exception e) {
                System.out.println("Ошибка при создании права: " + e.getMessage());
            }
        }
    }
}
