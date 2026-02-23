package io.allitov.rbac.filter.role;

import io.allitov.rbac.model.role.Permission;
import java.util.Objects;
import org.apache.commons.lang3.Strings;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Утилитарный класс, предоставляющий набор статических фабричных методов для создания объектов {@link RoleFilter}.
 * <p>
 * Данный класс не предназначен для создания экземпляров. Все методы возвращают неизменяемые
 * и "чистые" (pure) фильтры, предназначенные для проверки объектов ролей в рамках логики RBAC.
 */
public final class RoleFilters {

    private RoleFilters() {
        throw new IllegalStateException("No RoleFilters instances for you!");
    }

    /**
     * Создает фильтр для поиска роли по точному совпадению имени.
     *
     * @param name имя роли для сравнения.
     * @return {@link RoleFilter}, возвращающий {@code true}, если имя роли совпадает с аргументом.
     */
    @NotNull
    @Contract(pure = true)
    public static RoleFilter byName(@Nullable String name) {
        return role -> Objects.equals(role.getName(), name);
    }

    /**
     * Создает фильтр для поиска ролей, имя которых содержит указанную подстроку.
     * Сравнение выполняется без учета регистра (case-insensitive).
     *
     * @param substring подстрока для поиска.
     * @return {@link RoleFilter}, выполняющий поиск подстроки в имени роли.
     */
    @NotNull
    @Contract(pure = true)
    public static RoleFilter byNameContains(@Nullable String substring) {
        return role -> Strings.CI.contains(role.getName(), substring);
    }

    /**
     * Создает фильтр, проверяющий наличие у роли конкретного объекта разрешения.
     *
     * @param permission объект разрешения {@link Permission}.
     * @return {@link RoleFilter}, возвращающий {@code true}, если роль обладает данным разрешением.
     */
    @NotNull
    @Contract(pure = true)
    public static RoleFilter hasPermission(@Nullable Permission permission) {
        return role -> role.hasPermission(permission);
    }

    /**
     * Создает фильтр, проверяющий наличие у роли разрешения по его имени и целевому ресурсу.
     *
     * @param permissionName строковое имя разрешения.
     * @param resource       строковое представление ресурса.
     * @return {@link RoleFilter}, выполняющий поиск разрешения по строковым атрибутам.
     */
    @NotNull
    @Contract(pure = true)
    public static RoleFilter hasPermission(@Nullable String permissionName, @Nullable String resource) {
        return role -> role.hasPermission(permissionName, resource);
    }

    /**
     * Создает фильтр, проверяющий, что у роли количество разрешений не меньше заданного порога.
     *
     * @param n минимально допустимое количество разрешений (включительно).
     * @return {@link RoleFilter}, проверяющий размер коллекции разрешений роли.
     */
    @NotNull
    @Contract(pure = true)
    public static RoleFilter hasAtLeastNPermissions(int n) {
        return role -> role.getPermissions().size() >= n;
    }
}
