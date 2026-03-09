package io.allitov.rbac.command.impl.user;

import io.allitov.rbac.command.Command;
import io.allitov.rbac.filter.user.UserFilter;
import io.allitov.rbac.filter.user.UserFilters;
import io.allitov.rbac.model.user.User;
import io.allitov.rbac.system.RBACSystem;
import java.util.List;
import java.util.Scanner;

public class UserSearchCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        System.out.println("\n--- Выберите критерий поиска ---");
        System.out.println("1. По username (содержит)");
        System.out.println("2. По email");
        System.out.println("3. По домену email (например, gmail.com)");
        System.out.println("4. По полному имени (содержит)");
        System.out.print("Ваш выбор: ");

        String choice = scanner.nextLine();
        System.out.print("Введите значение для фильтра: ");
        String value = scanner.nextLine();

        UserFilter filter;
        switch (choice) {
            case "1" -> filter = UserFilters.byUsernameContains(value);
            case "2" -> filter = UserFilters.byEmail(value);
            case "3" -> filter = UserFilters.byEmailDomain(value);
            case "4" -> filter = UserFilters.byFullNameContains(value);
            default -> {
                System.err.println("Неверный выбор.");
                return;
            }
        }

        List<User> results = system.getUserManager().findByFilter(filter);

        if (results.isEmpty()) {
            System.out.println("Пользователи не найдены по заданным критериям.");
        } else {
            System.out.printf("%-20s | %-30s | %-30s%n", "Username", "Full Name", "Email");
            System.out.println("-".repeat(85));
            for (User user : results) {
                System.out.printf("%-20s | %-30s | %-30s%n", user.getUsername(), user.getFullName(), user.getEmail());
            }
        }
    }
}
