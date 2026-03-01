package io.allitov.command;

import io.allitov.system.RBACSystem;
import java.util.Scanner;

@FunctionalInterface
public interface Command {

    void execute(Scanner scanner, RBACSystem system);
}
