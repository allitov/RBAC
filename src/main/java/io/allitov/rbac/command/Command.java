package io.allitov.rbac.command;

import io.allitov.rbac.RBACSystem;
import java.util.Scanner;

@FunctionalInterface
public interface Command {

    void execute(Scanner scanner, RBACSystem system, String args);
}
