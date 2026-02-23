package io.allitov.rbac.filter.assignment;

import io.allitov.rbac.model.assignment.RoleAssignment;
import io.allitov.rbac.model.assignment.TemporaryAssignment;
import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;
import java.time.LocalDateTime;
import java.util.Objects;
import org.apache.commons.lang3.Strings;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Утилитарный класс, предоставляющий статические фабричные методы для создания объектов {@link AssignmentFilter}.
 * <p>
 * Класс не предназначен для создания экземпляров. Позволяет фильтровать назначения ролей (assignments) по пользователям, ролям, типам и временным рамкам.
 * Все методы возвращают иммутабельные фильтры, поддерживающие композицию.
 */
public final class AssignmentFilters {

    private AssignmentFilters() {
        throw new IllegalStateException("No AssignmentFilters instances for you!");
    }

    /**
     * Создает фильтр для поиска назначений по конкретному объекту пользователя.
     *
     * @param user объект пользователя.
     * @return {@link AssignmentFilter}, проверяющий равенство пользователя в назначении.
     */
    @NotNull
    @Contract(pure = true)
    public static AssignmentFilter byUser(@Nullable User user) {
        return assignment -> Objects.equals(assignment.user(), user);
    }

    /**
     * Создает фильтр для поиска назначений по уникальному имени пользователя (username).
     * Сравнение выполняется с учетом регистра (case-sensitive).
     *
     * @param username имя пользователя.
     * @return {@link AssignmentFilter}, проверяющий username пользователя в назначении.
     */
    @NotNull
    @Contract(pure = true)
    public static AssignmentFilter byUsername(@Nullable String username) {
        return assignment -> Strings.CS.equals(assignment.user().username(), username);
    }

    /**
     * Создает фильтр для поиска назначений по конкретному объекту роли.
     *
     * @param role объект роли.
     * @return {@link AssignmentFilter}, проверяющий равенство роли в назначении.
     */
    @NotNull
    @Contract(pure = true)
    public static AssignmentFilter byRole(@Nullable Role role) {
        return assignment -> Objects.equals(assignment.role(), role);
    }

    /**
     * Создает фильтр для поиска назначений по имени роли.
     * Сравнение выполняется с учетом регистра (case-sensitive).
     *
     * @param roleName имя роли.
     * @return {@link AssignmentFilter}, проверяющий имя роли.
     */
    @NotNull
    @Contract(pure = true)
    public static AssignmentFilter byRoleName(@Nullable String roleName) {
        return assignment -> Strings.CS.equals(assignment.role().getName(), roleName);
    }

    /**
     * Возвращает фильтр, пропускающий только активные назначения.
     *
     * @return {@link AssignmentFilter} для выбора активных ролей.
     */
    @NotNull
    @Contract(pure = true)
    public static AssignmentFilter activeOnly() {
        return RoleAssignment::isActive;
    }

    /**
     * Возвращает фильтр, пропускающий только неактивные назначения.
     *
     * @return {@link AssignmentFilter} для выбора отозванных или недействительных ролей.
     */
    @NotNull
    @Contract(pure = true)
    public static AssignmentFilter inactiveOnly() {
        return assignment -> !assignment.isActive();
    }

    /**
     * Создает фильтр по строковому представлению типа назначения.
     *
     * @param type тип назначения ("PERMANENT", "TEMPORARY").
     * @return {@link AssignmentFilter}, фильтрующий по полю {@code assignmentType}.
     */
    @NotNull
    @Contract(pure = true)
    public static AssignmentFilter byType(@Nullable String type) {
        return assignment -> Strings.CS.equals(assignment.assignmentType(), type);
    }

    /**
     * Создает фильтр по имени администратора, создавшего назначение.
     *
     * @param username имя (username) того, кто выдал роль.
     * @return {@link AssignmentFilter}, фильтрующий по метаданным создания.
     */
    @NotNull
    @Contract(pure = true)
    public static AssignmentFilter assignedBy(@Nullable String username) {
        return assignment -> Strings.CS.equals(assignment.metadata().assignedBy(), username);
    }

    /**
     * Создает фильтр, отбирающий назначения, созданные после указанной даты.
     * Ожидается дата в формате ISO (например, "2023-01-01T00:00:00").
     *
     * @param date строковое представление даты.
     * @return {@link AssignmentFilter}, проверяющий дату создания из метаданных.
     * @throws java.time.format.DateTimeParseException если формат даты невалиден.
     * @throws NullPointerException если {@code date} равен {@code null}.
     */
    @NotNull
    @Contract(pure = true)
    public static AssignmentFilter assignedAfter(@NotNull String date) {
        Objects.requireNonNull(date, "The 'date' must not be null");
        return assignment ->
                LocalDateTime.parse(assignment.metadata().assignedAt()).isAfter(LocalDateTime.parse(date));
    }

    /**
     * Создает фильтр для временных назначений, истекающих до указанной даты.
     * Постоянные назначения всегда игнорируются данным фильтром (возвращается {@code false}).
     * Ожидается дата в формате ISO (например, "2023-01-01T00:00:00").
     *
     * @param date строковое представление даты.
     * @return {@link AssignmentFilter}, проверяющий срок действия для {@link TemporaryAssignment}.
     * @throws java.time.format.DateTimeParseException если формат даты невалиден
     * @throws NullPointerException если {@code date} равен {@code null}.
     */
    @NotNull
    @Contract(pure = true)
    public static AssignmentFilter expiringBefore(@NotNull String date) {
        Objects.requireNonNull(date, "The 'date' must not be null");
        return assignment -> {
            if (assignment instanceof TemporaryAssignment ta) {
                return LocalDateTime.parse(ta.getExpiresAt()).isBefore(LocalDateTime.parse(date));
            }
            return false;
        };
    }
}
