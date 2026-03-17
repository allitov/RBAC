package io.allitov.rbac.model.user;

import io.allitov.rbac.validator.Validator;
import io.allitov.rbac.validator.impl.UserValidator;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Getter(AccessLevel.NONE)
    private static final Validator<User> VALIDATOR = new UserValidator();

    private final String username;
    private final String fullName;
    private final String email;

    public static User of(String username, String fullName, String email) {
        User newUser = new User(username, fullName, email);
        VALIDATOR.validate(newUser);
        return newUser;
    }

    public String format() {
        return "%s (%s) <%s>".formatted(username, fullName, email);
    }
}
