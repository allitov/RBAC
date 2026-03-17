package io.allitov.rbac.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FluentValidator<T> {

    private final T object;
    private final List<String> errors = new ArrayList<>();

    public static <T> FluentValidator<T> check(T object) {
        return new FluentValidator<>(object);
    }

    public FluentValidator<T> mustBe(Predicate<T> condition, String errorMessage) {
        if (!condition.test(object)) {
            errors.add(errorMessage);
        }
        return this;
    }

    public void validate() {
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public FluentValidator<T> isNotBlank(String errorMessage) {
        return mustBe(val -> val instanceof CharSequence cs && StringUtils.isNotBlank(cs), errorMessage);
    }

    public FluentValidator<T> matches(String regexp, String errorMessage) {
        return mustBe(val -> val instanceof String s && s.matches(regexp), errorMessage);
    }
}
