package io.allitov.rbac.command.impl.assignment;

import io.allitov.rbac.RBACSystem;
import io.allitov.rbac.command.Command;
import io.allitov.rbac.model.assignment.RoleAssignment;
import java.util.List;
import java.util.Scanner;

public class AssignmentActiveCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        List<RoleAssignment> activeAssignments = system.getAssignmentManager().getActiveAssignments();

        if (activeAssignments.isEmpty()) {
            System.out.println("Нет активных назначений в системе.");
            return;
        }

        System.out.println("Список активных назначений:");
        for (RoleAssignment assignment : activeAssignments) {
            System.out.println(assignment.metadata().format());
            System.out.println("-----------------------------------");
        }
    }
}
