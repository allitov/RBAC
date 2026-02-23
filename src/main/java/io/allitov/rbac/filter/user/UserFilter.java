package io.allitov.rbac.filter.user;

import io.allitov.rbac.model.user.User;
import java.util.Objects;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Функциональный интерфейс для определения условий фильтрации объектов {@link User}.
 * <p>
 * Используется для создания гибких предикатов в логике управления доступом.
 * Позволяет комбинировать несколько условий в цепочки с помощью методов {@link #and(UserFilter)}
 * и {@link #or(UserFilter)}.
 */
@FunctionalInterface
public interface UserFilter {

    /**
     * Проверяет, соответствует ли заданный пользователь условию фильтра.
     *
     * @param user объект пользователя для проверки.
     * @return {@code true}, если пользователь соответствует критерию, иначе {@code false}.
     */
    @Contract(pure = true)
    boolean test(@NotNull User user);

    /**
     * Возвращает составной фильтр, представляющий собой логическое "И" (AND)
     * между текущим фильтром и другим фильтром.
     *
     * @param other другой фильтр, который будет объединен с текущим.
     * @return новый экземпляр {@link UserFilter}, возвращающий {@code true} только если оба фильтра верны.
     * @throws NullPointerException если {@code other} равен {@code null}.
     */
    @NotNull
    @Contract(pure = true)
    default UserFilter and(@NotNull UserFilter other) {
        Objects.requireNonNull(other, "The 'other' user filter must not be null");
        return user -> test(user) && other.test(user);
    }

    /**
     * Возвращает составной фильтр, представляющий собой логическое "ИЛИ" (OR)
     * между текущим фильтром и другим фильтром.
     *
     * @param other другой фильтр, который будет объединен с текущим.
     * @return новый экземпляр {@link UserFilter}, возвращающий {@code true}, если хотя бы один из фильтров верен.
     * @throws NullPointerException если {@code other} равен {@code null}.
     */
    @NotNull
    @Contract(pure = true)
    default UserFilter or(@NotNull UserFilter other) {
        Objects.requireNonNull(other, "The 'other' user filter must not be null");
        return user -> test(user) || other.test(user);
    }
}
