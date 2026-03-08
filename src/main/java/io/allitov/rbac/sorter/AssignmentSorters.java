package io.allitov.rbac.sorter;

import io.allitov.rbac.model.assignment.RoleAssignment;
import java.time.LocalDateTime;
import java.util.Comparator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Утилитарный класс, предоставляющий набор статических методов для создания компараторов объектов {@link RoleAssignment}.
 * <p>
 * Позволяет упорядочивать коллекции назначений по различным полям.
 */
public final class AssignmentSorters {

    private AssignmentSorters() {
        throw new IllegalStateException("No AssignmentSorters instances for you!");
    }

    /**
     * Возвращает компаратор для сортировки назначений по имени пользователя (username) в алфавитном порядке.
     *
     * @return {@link Comparator} для сравнения назначений на основе {@code assignment.user().getUsername()}.
     */
    @NotNull
    @Contract(pure = true)
    public static Comparator<RoleAssignment> byUsername() {
        return Comparator.comparing(assignment -> assignment.user().getUsername());
    }

    /**
     * Возвращает компаратор для сортировки назначений по имени роли в алфавитном порядке.
     *
     * @return {@link Comparator} для сравнения назначений на основе {@code assignment.role().getName()}.
     */
    @NotNull
    @Contract(pure = true)
    public static Comparator<RoleAssignment> byRoleName() {
        return Comparator.comparing(assignment -> assignment.role().getName());
    }

    /**
     * Возвращает компаратор для сортировки назначений по дате их создания.
     * <p>
     * Сортировка производится от более старых назначений к более новым.
     *
     * @return {@link Comparator}, сравнивающий даты из {@code assignment.metadata().getAssignedAt()}.
     */
    @NotNull
    @Contract(pure = true)
    public static Comparator<RoleAssignment> byAssignmentDate() {
        return (a1, a2) -> {
            LocalDateTime d1 = a1.metadata().getAssignedAt();
            LocalDateTime d2 = a2.metadata().getAssignedAt();
            return d1.compareTo(d2);
        };
    }
}
