package io.allitov.rbac.command;

import io.allitov.rbac.command.impl.user.UserCreateCommand;
import io.allitov.rbac.command.impl.user.UserDeleteCommand;
import io.allitov.rbac.command.impl.user.UserListCommand;
import io.allitov.rbac.command.impl.user.UserSearchCommand;
import io.allitov.rbac.command.impl.user.UserUpdateCommand;
import io.allitov.rbac.command.impl.user.UserViewCommand;

public final class CommandRegistry {

    private CommandRegistry() {
        throw new IllegalStateException("No CommandRegistry instances found!");
    }

    public static void registerCommands(CommandParser parser) {
        // user commands
        parser.registerCommand(
                "user-list", "Вывести список всех пользователей. Аргументы: username.", new UserListCommand());
        parser.registerCommand(
                "user-create",
                "Создать нового пользователя. Аргументы: username fullName email.",
                new UserCreateCommand());
        parser.registerCommand(
                "user-view", "Просмотр информации о пользователе. Аргументы: username.", new UserViewCommand());
        parser.registerCommand(
                "user-update", "Обновить данные пользователя. Аргументы: username.", new UserUpdateCommand());
        parser.registerCommand("user-delete", "Удалить пользователя. Аргументы: username.", new UserDeleteCommand());
        parser.registerCommand("user-search", "Найти всех пользователей по фильтрам.", new UserSearchCommand());

        // util commands
        parser.registerCommand("help", "Выводит все команды с описанием.", (_, _, _) -> parser.printHelp());
        parser.registerCommand("stats", "Выводит статистику системы.", (_, system, _) -> system.generateStatistics());
        parser.registerCommand("exit", "Выйти из программы.", (_, _, _) -> System.exit(0));
    }
}
