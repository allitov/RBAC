package io.allitov.rbac.filter.user;

import java.util.Objects;
import org.apache.commons.lang3.Strings;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Набор статических фильтров для работы с объектами {@code User}.
 * <p>
 * Класс является утилитарным и не предназначен для создания экземпляров.
 * Все методы возвращают функциональный интерфейс {@link UserFilter}.
 */
public final class UserFilters {

    private UserFilters() {
        throw new IllegalStateException("No UserFilters instances for you!");
    }

    /**
     * Создает фильтр для поиска пользователя по точному совпадению имени (username).
     *
     * @param username имя пользователя для сравнения.
     * @return фильтр, проверяющий username.
     */
    @NotNull
    @Contract(pure = true)
    public static UserFilter byUsername(@Nullable String username) {
        return user -> Objects.equals(user.username(), username);
    }

    /**
     * Создает фильтр для поиска пользователя, чье имя (username) содержит указанную подстроку.
     * Поиск выполняется без учета регистра (Case-Insensitive).
     *
     * @param substring подстрока для поиска.
     * @return фильтр для частичного поиска по username.
     */
    @NotNull
    @Contract(pure = true)
    public static UserFilter byUsernameContains(@Nullable String substring) {
        return user -> Strings.CI.contains(user.username(), substring);
    }

    /**
     * Создает фильтр для поиска пользователя по точному совпадению email.
     *
     * @param email адрес электронной почты.
     * @return фильтр, проверяющий email.
     */
    @NotNull
    @Contract(pure = true)
    public static UserFilter byEmail(String email) {
        return user -> Objects.equals(user.email(), email);
    }

    /**
     * Создает фильтр для фильтрации пользователей по домену электронной почты.
     * Сравнение выполняется с учетом регистра (Case-Sensitive).
     *
     * @param domain домен (например, "gmail.com").
     * @return фильтр, проверяющий окончание адреса электронной почты.
     */
    @NotNull
    @Contract(pure = true)
    public static UserFilter byEmailDomain(@Nullable String domain) {
        return user -> Strings.CS.endsWith(user.email(), domain);
    }

    /**
     * Создает фильтр для поиска по полному имени пользователя.
     * Сравнение выполняется с учетом регистра (Case-Sensitive).
     *
     * @param substring подстрока для поиска в полном имени.
     * @return фильтр для частичного поиска по full name.
     */
    @NotNull
    @Contract(pure = true)
    public static UserFilter byFullNameContains(@Nullable String substring) {
        return user -> Strings.CS.contains(user.fullName(), substring);
    }
}
