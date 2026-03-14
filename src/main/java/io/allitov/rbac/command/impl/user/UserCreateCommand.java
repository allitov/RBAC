package io.allitov.rbac.command.impl.user;

import io.allitov.rbac.RBACSystem;
import io.allitov.rbac.command.Command;
import io.allitov.rbac.model.user.User;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

public class UserCreateCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        String username;
        String fullName;
        String email;

        if (!StringUtils.isBlank(args)) {
            String[] parts = args.split("\\s+", 3);
            if (parts.length == 3) {
                username = parts[0];
                fullName = parts[1];
                email = parts[2];
            } else {
                IO.println("Ошибка: Ожидалось 3 аргумента (username, fullName, email).");
                return;
            }
        } else {
            IO.print("Username: ");
            username = scanner.nextLine();
            IO.print("Full name: ");
            fullName = scanner.nextLine();
            IO.print("Email: ");
            email = scanner.nextLine();
        }

        try {
            system.getUserManager().add(User.of(username, fullName, email));
            IO.println("Пользователь " + username + " успешно создан.");
        } catch (RuntimeException e) {
            IO.println("Ошибка: " + e.getMessage());
        }
    }
}
