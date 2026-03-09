package io.allitov.rbac.command.impl.user;

import io.allitov.rbac.command.Command;
import io.allitov.rbac.filter.user.UserFilters;
import io.allitov.rbac.model.user.User;
import io.allitov.rbac.system.RBACSystem;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

public class UserListCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        List<User> users;

        if (!StringUtils.isBlank(args)) {
            String filterParam = args.trim();
            users = system.getUserManager().findByFilter(UserFilters.byUsernameContains(filterParam));
        } else {
            users = system.getUserManager().findAll();
        }

        if (users.isEmpty()) {
            IO.println("Пользователи не найдены.");
            return;
        }

        System.out.printf("%-20s | %-30s | %-30s%n", "Username", "Full Name", "Email");
        System.out.println("-".repeat(85));

        for (User user : users) {
            System.out.printf("%-20s | %-30s | %-30s%n", user.getUsername(), user.getFullName(), user.getEmail());
        }
    }
}
