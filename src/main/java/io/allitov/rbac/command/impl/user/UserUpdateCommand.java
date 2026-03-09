package io.allitov.rbac.command.impl.user;

import io.allitov.rbac.command.Command;
import io.allitov.rbac.system.RBACSystem;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

public class UserUpdateCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        String username;

        if (!StringUtils.isBlank(args)) {
            username = args.trim();
        } else {
            System.out.print("Введите username пользователя, которого нужно обновить: ");
            username = scanner.nextLine();
        }

        if (!system.getUserManager().existsByUsername(username)) {
            System.err.println("Ошибка: Пользователь '" + username + "' не найден.");
            return;
        }

        System.out.println("Введите новые данные:");
        System.out.print("Новое полное имя: ");
        String fullName = scanner.nextLine();
        System.out.print("Новый email: ");
        String email = scanner.nextLine();

        try {
            system.getUserManager().update(username, fullName, email);
            System.out.println("Успех: Данные пользователя " + username + " обновлены.");
        } catch (RuntimeException e) {
            System.err.println("Ошибка при обновлении: " + e.getMessage());
        }
    }
}
