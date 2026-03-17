package io.allitov.rbac.validator;

import io.allitov.rbac.validator.rule.ValidationRule;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация {@link Validator}, объединяющая несколько правил валидации
 * в одну цепочку проверок.
 * <p>
 * Данный класс следует паттерну "Компоновщик" и позволяет собирать произвольный
 * набор объектов {@link ValidationRule} для комплексной проверки объекта типа {@code T}.
 * В процессе валидации выполняются все зарегистрированные правила, а накопленные
 * ошибки агрегируются в единое исключение {@link ValidationException}.
 *
 * @param <T> Тип объекта, подлежащего валидации.
 */
public abstract class CompositeValidator<T> implements Validator<T> {

    private final List<ValidationRule<T>> rules = new ArrayList<>();

    /**
     * Добавляет правило валидации в цепочку проверок.
     *
     * @param rule Правило, которое будет добавлено. Не {@code null}.
     * @return Текущий экземпляр {@code CompositeValidator}.
     */
    public CompositeValidator<T> addRule(ValidationRule<T> rule) {
        rules.add(rule);
        return this;
    }

    /**
     * Выполняет все добавленные правила валидации последовательно.
     *
     * @param obj Объект для проверки.
     * @throws ValidationException Если хотя бы одно из правил завершилось
     * неудачей. Список всех обнаруженных ошибок доступен через {@link ValidationException#getErrors()}.
     */
    @Override
    public void validate(T obj) throws ValidationException {
        List<String> errors = new ArrayList<>();
        for (ValidationRule<T> rule : rules) {
            try {
                rule.check(obj);
            } catch (IllegalArgumentException e) {
                errors.add(e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
