package io.allitov.rbac.command;

import io.allitov.rbac.command.impl.role.RoleAddPermissionCommand;
import io.allitov.rbac.command.impl.role.RoleCreateCommand;
import io.allitov.rbac.command.impl.role.RoleDeleteCommand;
import io.allitov.rbac.command.impl.role.RoleListCommand;
import io.allitov.rbac.command.impl.role.RoleRemovePermissionCommand;
import io.allitov.rbac.command.impl.role.RoleUpdateCommand;
import io.allitov.rbac.command.impl.role.RoleViewCommand;
import io.allitov.rbac.command.impl.user.UserCreateCommand;
import io.allitov.rbac.command.impl.user.UserDeleteCommand;
import io.allitov.rbac.command.impl.user.UserListCommand;
import io.allitov.rbac.command.impl.user.UserSearchCommand;
import io.allitov.rbac.command.impl.user.UserUpdateCommand;
import io.allitov.rbac.command.impl.user.UserViewCommand;
import io.allitov.rbac.log.AuditLog;

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

        // role commands
        parser.registerCommand("role-list", "Вывести список всех ролей.", new RoleListCommand());
        parser.registerCommand("role-create", "Создать новую роль с правами.", new RoleCreateCommand());
        parser.registerCommand("role-view", "Просмотреть детальную информацию о роли.", new RoleViewCommand());
        parser.registerCommand("role-update", "Изменить название или описание роли.", new RoleUpdateCommand());
        parser.registerCommand("role-delete", "Удалить роль по названию.", new RoleDeleteCommand());
        parser.registerCommand(
                "role-add-permission", "Добавить новое право к существующей роли.", new RoleAddPermissionCommand());
        parser.registerCommand(
                "role-remove-permission",
                "Удалить конкретное право из роли по номеру.",
                new RoleRemovePermissionCommand());

        // util commands
        parser.registerCommand("help", "Выводит все команды с описанием.", (_, _, _) -> parser.printHelp());
        parser.registerCommand("stats", "Выводит статистику системы.", (_, system, _) -> system.generateStatistics());
        parser.registerCommand("exit", "Выйти из программы.", (_, _, _) -> System.exit(0));
        parser.registerCommand("audit-log", "Просмотр логов.", (_, _, _) -> AuditLog.printLog());
    }
}
