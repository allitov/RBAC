package io.allitov.rbac.filter.assignment;

import io.allitov.rbac.model.assignment.RoleAssignment;
import java.util.Objects;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Функциональный интерфейс для определения условий фильтрации объектов {@link RoleAssignment}.
 * <p>
 * Используется для создания гибких предикатов в логике управления доступом.
 * Позволяет комбинировать несколько условий в цепочки с помощью методов {@link #and(AssignmentFilter)}
 * и {@link #or(AssignmentFilter)}.
 */
@FunctionalInterface
public interface AssignmentFilter {

    /**
     * Проверяет, соответствует ли заданное назначение условию фильтра.
     *
     * @param assignment объект назначения для проверки.
     * @return {@code true}, если назначение соответствует критерию, иначе {@code false}.
     */
    @Contract(pure = true)
    boolean test(@NotNull RoleAssignment assignment);

    /**
     * Возвращает составной фильтр, представляющий собой логическое "И" (AND)
     * между текущим фильтром и другим фильтром.
     *
     * @param other другой фильтр, который будет объединен с текущим.
     * @return новый экземпляр {@link AssignmentFilter}, возвращающий {@code true} только если оба фильтра верны.
     * @throws NullPointerException если {@code other} равен {@code null}.
     */
    @NotNull
    @Contract(pure = true)
    default AssignmentFilter and(@NotNull AssignmentFilter other) {
        Objects.requireNonNull(other, "The 'other' filter must not be null");
        return assignment -> test(assignment) && other.test(assignment);
    }

    /**
     * Возвращает составной фильтр, представляющий собой логическое "ИЛИ" (OR)
     * между текущим фильтром и другим фильтром.
     *
     * @param other другой фильтр, который будет объединен с текущим.
     * @return новый экземпляр {@link AssignmentFilter}, возвращающий {@code true}, если хотя бы один из фильтров верен.
     * @throws NullPointerException если {@code other} равен {@code null}.
     */
    @NotNull
    @Contract(pure = true)
    default AssignmentFilter or(@NotNull AssignmentFilter other) {
        Objects.requireNonNull(other, "The 'other' filter must not be null");
        return assignment -> test(assignment) || other.test(assignment);
    }
}
