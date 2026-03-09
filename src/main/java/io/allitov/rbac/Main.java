package io.allitov.rbac;

import io.allitov.rbac.command.CommandParser;
import io.allitov.rbac.command.CommandRegistry;
import io.allitov.rbac.system.RBACSystem;
import java.util.Scanner;

public class Main {

    static void main() {
        RBACSystem system = new RBACSystem();
        system.initialize();

        CommandParser parser = new CommandParser();
        CommandRegistry.registerCommands(parser);

        Scanner scanner = new Scanner(System.in);
        IO.println("RBAC System Console. Type 'help' for commands.");

        while (true) {
            IO.print("> ");
            String input = scanner.nextLine();
            parser.parseAndExecute(input, scanner, system);
        }
    }
}
