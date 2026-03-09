package io.allitov.rbac.sorter;

import io.allitov.rbac.model.user.User;
import java.util.Comparator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Утилитарный класс, предоставляющий набор статических методов для создания компараторов объектов {@link User}.
 * <p>
 * Позволяет упорядочивать коллекции пользователей по различным полям.
 */
public final class UserSorters {

    private UserSorters() {
        throw new IllegalStateException("No UserSorters instances for you!");
    }

    /**
     * Возвращает компаратор для сортировки пользователей по имени пользователя (username) в алфавитном порядке.
     *
     * @return {@link Comparator} для сравнения пользователей по полю {@code username}.
     */
    @NotNull
    @Contract(pure = true)
    public static Comparator<User> byUsername() {
        return Comparator.comparing(User::getUsername);
    }

    /**
     * Возвращает компаратор для сортировки пользователей по полному имени (fullName) в алфавитном порядке.
     *
     * @return {@link Comparator} для сравнения пользователей по полю {@code fullName}.
     */
    @NotNull
    @Contract(pure = true)
    public static Comparator<User> byFullName() {
        return Comparator.comparing(User::getFullName);
    }

    /**
     * Возвращает компаратор для сортировки пользователей по адресу электронной почты (email) в алфавитном порядке.
     *
     * @return {@link Comparator} для сравнения пользователей по полю {@code email}.
     */
    @NotNull
    @Contract(pure = true)
    public static Comparator<User> byEmail() {
        return Comparator.comparing(User::getEmail);
    }
}
