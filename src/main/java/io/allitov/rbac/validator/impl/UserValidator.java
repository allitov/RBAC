package io.allitov.rbac.validator.impl;

import io.allitov.rbac.model.user.User;
import io.allitov.rbac.validator.CompositeValidator;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * Реализация валидатора для сущности {@link User}.
 * <p>
 * Данный класс настраивает цепочку правил проверки для полей пользователя, включая проверку на наличие данных и
 * соответствие строковым паттернам (регулярным выражениям).
 */
public class UserValidator extends CompositeValidator<User> {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public UserValidator() {
        this.addRule(u -> {
                    if (StringUtils.isBlank(u.getUsername())) {
                        throw new IllegalArgumentException(
                                "Username must not be null or blank. Given: '%s'".formatted(u.getUsername()));
                    }
                })
                .addRule(u -> {
                    if (!USERNAME_PATTERN.matcher(u.getUsername()).matches()) {
                        throw new IllegalArgumentException("Username must match regex: '%s'. Given: '%s'"
                                .formatted(USERNAME_PATTERN.pattern(), u.getUsername()));
                    }
                })
                .addRule(u -> {
                    if (StringUtils.isBlank(u.getFullName())) {
                        throw new IllegalArgumentException(
                                "Full name must not be null or blank. Given: '%s'".formatted(u.getUsername()));
                    }
                })
                .addRule(u -> {
                    if (StringUtils.isBlank(u.getEmail())) {
                        throw new IllegalArgumentException(
                                "Email must not be null or blank. Given: '%s'".formatted(u.getEmail()));
                    }
                })
                .addRule(u -> {
                    if (!EMAIL_PATTERN.matcher(u.getEmail()).matches()) {
                        throw new IllegalArgumentException("Email must match regex: '%s'. Given: '%s'"
                                .formatted(EMAIL_PATTERN.pattern(), u.getEmail()));
                    }
                });
    }
}
