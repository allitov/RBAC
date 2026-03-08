package io.allitov.rbac.filter.user;

import java.util.Objects;
import org.apache.commons.lang3.Strings;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Утилитарный класс, предоставляющий набор статических фабричных методов для создания объектов {@link UserFilter}.
 * <p>
 * Класс не предназначен для создания экземпляров. Все методы возвращают неизменяемые
 * фильтры и "чистые" (pure) фильтры, предназначенные для проверки объектов пользователей в рамках логики RBAC.
 */
public final class UserFilters {

    private UserFilters() {
        throw new IllegalStateException("No UserFilters instances for you!");
    }

    /**
     * Создает фильтр для поиска пользователя по точному совпадению имени пользователя (username).
     *
     * @param username имя пользователя для сравнения.
     * @return {@link UserFilter}, возвращающий {@code true}, если {@code username} совпадает с именем пользователя.
     */
    @NotNull
    @Contract(pure = true)
    public static UserFilter byUsername(@Nullable String username) {
        return user -> Objects.equals(user.getUsername(), username);
    }

    /**
     * Создает фильтр для поиска пользователя, чье имя (username) содержит указанную подстроку.
     * <p>
     * Поиск выполняется без учета регистра (case-insensitive).
     *
     * @param substring подстрока для поиска.
     * @return {@link UserFilter} для частичного поиска по имени пользователя без учета регистра.
     */
    @NotNull
    @Contract(pure = true)
    public static UserFilter byUsernameContains(@Nullable String substring) {
        return user -> Strings.CI.contains(user.getUsername(), substring);
    }

    /**
     * Создает фильтр для поиска пользователя по точному совпадению адреса электронной почты.
     *
     * @param email адрес электронной почты для сравнения.
     * @return {@link UserFilter}, проверяющий полное совпадение email.
     */
    @NotNull
    @Contract(pure = true)
    public static UserFilter byEmail(@Nullable String email) {
        return user -> Objects.equals(user.getEmail(), email);
    }

    /**
     * Создает фильтр для фильтрации пользователей по домену электронной почты.
     * <p>
     * Сравнение выполняется с учетом регистра (case-sensitive).
     *
     * @param domain домен (например, "gmail.com").
     * @return {@link UserFilter}, проверяющий, заканчивается ли email пользователя на указанный домен.
     */
    @NotNull
    @Contract(pure = true)
    public static UserFilter byEmailDomain(@Nullable String domain) {
        return user -> Strings.CS.endsWith(user.getEmail(), domain);
    }

    /**
     * Создает фильтр для поиска по полному имени пользователя.
     * <p>
     * Сравнение выполняется с учетом регистра (case-sensitive).
     *
     * @param substring подстрока для поиска в полном имени.
     * @return {@link UserFilter} для частичного поиска в полном имени пользователя.
     */
    @NotNull
    @Contract(pure = true)
    public static UserFilter byFullNameContains(@Nullable String substring) {
        return user -> Strings.CS.contains(user.getFullName(), substring);
    }
}
