package io.allitov.rbac.filter.role;

import io.allitov.rbac.model.role.Role;
import java.util.Objects;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Функциональный интерфейс для определения условий фильтрации объектов {@link Role}.
 * <p>
 * Используется для создания гибких предикатов в логике управления доступом.
 * Позволяет комбинировать несколько условий в цепочки с помощью методов {@link #and(RoleFilter)}
 * и {@link #or(RoleFilter)}.
 */
@FunctionalInterface
public interface RoleFilter {

    /**
     * Проверяет, соответствует ли заданная роль условию фильтра.
     *
     * @param role объект роли для проверки.
     * @return {@code true}, если роль соответствует критерию, иначе {@code false}.
     */
    @Contract(pure = true)
    boolean test(@NotNull Role role);

    /**
     * Возвращает составной фильтр, представляющий собой логическое "И" (AND)
     * между текущим фильтром и другим фильтром.
     *
     * @param other другой фильтр, который будет объединен с текущим.
     * @return новый экземпляр {@link RoleFilter}, возвращающий {@code true} только если оба фильтра верны.
     * @throws NullPointerException если {@code other} равен {@code null}.
     */
    @NotNull
    @Contract(pure = true)
    default RoleFilter and(@NotNull RoleFilter other) {
        Objects.requireNonNull(other, "The 'other' filter must not be null");
        return role -> test(role) && other.test(role);
    }

    /**
     * Возвращает составной фильтр, представляющий собой логическое "ИЛИ" (OR)
     * между текущим фильтром и другим фильтром.
     *
     * @param other другой фильтр, который будет объединен с текущим.
     * @return новый экземпляр {@link RoleFilter}, возвращающий {@code true}, если хотя бы один из фильтров верен.
     * @throws NullPointerException если {@code other} равен {@code null}.
     */
    @NotNull
    @Contract(pure = true)
    default RoleFilter or(@NotNull RoleFilter other) {
        Objects.requireNonNull(other, "The 'other' filter must not be null");
        return role -> test(role) || other.test(role);
    }
}
