package io.allitov.rbac.command;

import io.allitov.rbac.RBACSystem;
import io.allitov.rbac.log.AuditLog;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

public class CommandParser {

    private final Map<String, Command> commands = new LinkedHashMap<>();
    private final Map<String, String> commandDescriptions = new HashMap<>();

    public void registerCommand(String name, String description, Command command) {
        commands.put(name, command);
        commandDescriptions.put(name, description);
    }

    public void printHelp() {
        StringBuilder help = new StringBuilder();
        for (String name : commands.keySet()) {
            String commandInfo = name + " - " + commandDescriptions.get(name);
            help.append(commandInfo).append(System.lineSeparator());
        }
        IO.print(help.toString());
    }

    public void parseAndExecute(String input, Scanner scanner, RBACSystem system) {
        AuditLog.log(input, system.getCurrentUser(), "RBAC system", "Details");
        if (StringUtils.isBlank(input)) {
            IO.println("Введена пустая команда.");
            return;
        }

        String[] parts = input.trim().split("\\s+", 2);
        String cmdName = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

        Command command = commands.get(cmdName);
        if (command != null) {
            command.execute(scanner, system, args);
        } else {
            IO.println("Введена несуществующая команда.");
        }
    }
}
