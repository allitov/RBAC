package io.allitov.rbac.model.validator;

/**
 * Функциональный интерфейс для определения правил валидации объекта.
 * <p>
 * Используется для инкапсуляции логики проверки конкретного условия.
 * Реализации данного интерфейса должны быть идемпотентными и не изменять
 * состояние целевого объекта.
 *
 * @param <T> Тип объекта, подлежащего проверке.
 */
@FunctionalInterface
public interface ValidationRule<T> {

    /**
     * Выполняет проверку целевого объекта.
     *
     * @param target Объект для проверки. Не {@code null}.
     * @throws IllegalArgumentException Если объект не соответствует правилу валидации.
     */
    void check(T target) throws IllegalArgumentException;
}
