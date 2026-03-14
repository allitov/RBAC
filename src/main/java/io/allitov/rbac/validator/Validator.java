package io.allitov.rbac.validator;

import io.allitov.rbac.validator.rule.ValidationRule;

/**
 * Контракт для реализации логики валидации объектов типа {@code T}.
 * <p>
 * Реализации данного интерфейса отвечают за проверку целостности данных
 * и бизнес-логики объекта. В отличие от {@link ValidationRule}, этот интерфейс
 * может объединять несколько правил и агрегировать результаты проверок.
 * @param <T> Тип объекта, подлежащего валидации.
 */
public interface Validator<T> {

    /**
     * Выполняет проверку объекта.
     * @param obj Объект для валидации. Не {@code null}.
     * @throws ValidationException Если объект не прошел проверку.
     * Исключение должно содержать детали о всех обнаруженных нарушениях.
     */
    void validate(T obj) throws ValidationException;
}
