package io.allitov.rbac.validator.impl;

import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.validator.CompositeValidator;
import org.apache.commons.lang3.StringUtils;

/**
 * Реализация валидатора для сущности {@link Role}.
 * <p>
 * Данный класс настраивает цепочку правил проверки для полей роли, включая проверку на наличие данных.
 */
public class RoleValidator extends CompositeValidator<Role> {

    public RoleValidator() {
        this.addRule(r -> {
                    if (StringUtils.isBlank(r.getName())) {
                        throw new IllegalArgumentException(
                                "Name must not be null or blank. Given: '%s'".formatted(r.getName()));
                    }
                })
                .addRule(r -> {
                    if (StringUtils.isBlank(r.getDescription())) {
                        throw new IllegalArgumentException(
                                "Description must not be null or blank. Given: '%s'".formatted(r.getDescription()));
                    }
                });
    }
}
