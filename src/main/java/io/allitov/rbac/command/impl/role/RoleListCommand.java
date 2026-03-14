package io.allitov.rbac.command.impl.role;

import io.allitov.rbac.RBACSystem;
import io.allitov.rbac.command.Command;
import io.allitov.rbac.model.role.Role;
import java.util.List;
import java.util.Scanner;

public class RoleListCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        List<Role> roles = system.getRoleManager().findAll();

        if (roles.isEmpty()) {
            System.out.println("Список ролей пуст.");
            return;
        }

        System.out.println("Список доступных ролей:");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-20s | %-15s | %s%n", "Название", "Кол-во прав", "ID");
        System.out.println("------------------------------------------------------------");

        for (Role role : roles) {
            System.out.printf(
                    "%-20s | %-15d | %s%n",
                    role.getName(), role.getPermissions().size(), role.getId());
        }
        System.out.println("------------------------------------------------------------");
    }
}
