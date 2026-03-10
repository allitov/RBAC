package io.allitov.rbac.command.impl.user;

import io.allitov.rbac.command.Command;
import io.allitov.rbac.system.RBACSystem;
import io.allitov.rbac.util.ConsoleUtils;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

public class UserUpdateCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        String username;

        if (!StringUtils.isBlank(args)) {
            username = args.trim();
        } else {
            username =
                    ConsoleUtils.promptString(scanner, "Введите username пользователя, которого нужно обновить.", true);
        }

        if (!system.getUserManager().existsByUsername(username)) {
            System.err.println("Ошибка: Пользователь '" + username + "' не найден.");
            return;
        }

        System.out.println("Введите новые данные:");
        String fullName = ConsoleUtils.promptString(scanner, "Новое полное имя", true);
        String email = ConsoleUtils.promptString(scanner, "Новый email", true);

        try {
            system.getUserManager().update(username, fullName, email);
            System.out.println("Успех: Данные пользователя " + username + " обновлены.");
        } catch (RuntimeException e) {
            System.err.println("Ошибка при обновлении: " + e.getMessage());
        }
    }
}
