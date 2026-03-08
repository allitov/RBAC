package io.allitov.rbac.model.validator;

import java.util.List;

public class ValidationException extends RuntimeException {

    private final List<String> errors;

    public ValidationException(List<String> errors) {
        super("Validation failed with %d errors: %s".formatted(errors.size(), String.join(", ", errors)));
        this.errors = errors;
    }

    public List<String> getErrors() {
        return List.copyOf(errors);
    }
}
