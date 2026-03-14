package io.allitov.rbac.command.impl.role;

import io.allitov.rbac.RBACSystem;
import io.allitov.rbac.command.Command;
import io.allitov.rbac.model.role.Role;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class RoleDeleteCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        String roleName;

        if (args.isBlank()) {
            System.out.print("Введите название роли для удаления: ");
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

            System.out.print("Вы уверены, что хотите безвозвратно удалить роль '" + roleName + "'? (y/n): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("y") || confirmation.equals("д")) {
                boolean removed = system.getRoleManager().remove(role);

                if (removed) {
                    System.out.println("Роль успешно удалена.");
                } else {
                    System.out.println("Не удалось удалить роль (возможно, она уже была удалена).");
                }
            } else {
                System.out.println("Удаление отменено.");
            }

        } catch (NoSuchElementException e) {
            System.out.println("Ошибка: Роль с названием '" + roleName + "' не найдена.");
        }
    }
}
