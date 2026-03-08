package io.allitov.rbac.model.validator.impl;

import io.allitov.rbac.model.role.Permission;
import io.allitov.rbac.model.validator.CompositeValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

/**
 * Реализация валидатора для сущности {@link Permission}.
 * <p>
 * Данный класс настраивает цепочку правил проверки для полей права доступа, включая проверку на наличие данных.
 */
public class PermissionValidator extends CompositeValidator<Permission> {

    public PermissionValidator() {
        this.addRule(p -> {
                    if (StringUtils.isBlank(p.getName())) {
                        throw new IllegalArgumentException(
                                "Name must not be null or blank. Given: '%s'".formatted(p.getName()));
                    }
                })
                .addRule(p -> {
                    if (Strings.CS.contains(p.getName(), " ")) {
                        throw new IllegalArgumentException(
                                "Name must not contain spaces. Given: '%s'".formatted(p.getName()));
                    }
                })
                .addRule(p -> {
                    if (StringUtils.isBlank(p.getResource())) {
                        throw new IllegalArgumentException(
                                "Resource must not be null or blank. Given: '%s'".formatted(p.getResource()));
                    }
                })
                .addRule(p -> {
                    if (StringUtils.isBlank(p.getDescription())) {
                        throw new IllegalArgumentException(
                                "Description must not be null or blank. Given: '%s'".formatted(p.getDescription()));
                    }
                });
    }
}
