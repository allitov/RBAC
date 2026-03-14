package io.allitov.rbac.command.impl.assignment;

import io.allitov.rbac.RBACSystem;
import io.allitov.rbac.command.Command;
import io.allitov.rbac.model.assignment.TemporaryAssignment;
import java.util.List;
import java.util.Scanner;

public class AssignmentExpiredCommand implements Command {

    @Override
    public void execute(Scanner scanner, RBACSystem system, String args) {
        List<TemporaryAssignment> expiredAssignments =
                system.getAssignmentManager().getExpiredAssignments();

        if (expiredAssignments.isEmpty()) {
            System.out.println("Истёкших временных назначений не найдено.");
            return;
        }

        System.out.println("Список истёкших временных назначений:");
        for (TemporaryAssignment assignment : expiredAssignments) {
            System.out.println(assignment.summary());
            System.out.println("Срок действия истёк: " + assignment.getExpiresAt());
            System.out.println("-----------------------------------");
        }
    }
}
