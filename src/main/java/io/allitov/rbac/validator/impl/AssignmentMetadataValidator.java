package io.allitov.rbac.validator.impl;

import io.allitov.rbac.model.assignment.AssignmentMetadata;
import io.allitov.rbac.validator.CompositeValidator;
import org.apache.commons.lang3.StringUtils;

/**
 * Реализация валидатора для сущности {@link AssignmentMetadata}.
 * <p>
 * Данный класс настраивает цепочку правил проверки для полей назначения, включая проверку на наличие данных.
 */
public class AssignmentMetadataValidator extends CompositeValidator<AssignmentMetadata> {

    public AssignmentMetadataValidator() {
        this.addRule(m -> {
                    if (StringUtils.isBlank(m.getAssignedBy())) {
                        throw new IllegalArgumentException(
                                "Assigner name must not be null or blank. Given: '%s'".formatted(m.getAssignedBy()));
                    }
                })
                .addRule(m -> {
                    if (StringUtils.isBlank(m.getReason())) {
                        throw new IllegalArgumentException(
                                "Reason must not be null or blank. Given: '%s'".formatted(m.getReason()));
                    }
                });
    }
}
