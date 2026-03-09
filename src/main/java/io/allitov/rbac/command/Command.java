package io.allitov.rbac.command;

import io.allitov.rbac.system.RBACSystem;
import java.util.Scanner;

@FunctionalInterface
public interface Command {

    void execute(Scanner scanner, RBACSystem system, String args);
}
