package io.allitov.rbac.sorter.role;

import io.allitov.rbac.model.role.Role;
import java.util.Comparator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Утилитарный класс, предоставляющий набор статических методов для создания компараторов объектов {@link Role}.
 * <p>
 * Позволяет упорядочивать коллекции ролей по различным полям.
 */
public final class RoleSorters {

    private RoleSorters() {
        throw new IllegalStateException("No RoleSorters instances for you!");
    }

    /**
     * Возвращает компаратор для сортировки ролей по их имени в алфавитном порядке.
     *
     * @return {@link Comparator} для сравнения ролей по полю {@code name}.
     */
    @NotNull
    @Contract(pure = true)
    public static Comparator<Role> byName() {
        return Comparator.comparing(Role::getName);
    }

    /**
     * Возвращает компаратор для сортировки ролей по количеству связанных с ними разрешений (permissions).
     * <p>
     * Сортировка производится по возрастанию количества разрешений.
     *
     * @return {@link Comparator} для сравнения ролей по размеру коллекции разрешений.
     */
    @NotNull
    @Contract(pure = true)
    public static Comparator<Role> byPermissionCount() {
        return Comparator.comparingInt(role -> role.getPermissions().size());
    }
}
