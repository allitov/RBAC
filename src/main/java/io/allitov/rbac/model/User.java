package io.allitov.rbac.model;

import java.util.regex.Pattern;

public record User(String username, String fullName, String email) {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public User {
        username = normalize(username);
        fullName = normalize(fullName);
        email = normalize(email);
        validate(username, fullName, email);
    }

    public String format() {
        return String.format("%s (%s) <%s>", username, fullName, email);
    }

    private String normalize(String value) {
        return value != null ? value.trim() : null;
    }

    private void validate(String username, String fullName, String email) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(
                    String.format("Username must not be null or blank. Given: '%s'", username));
        }

        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new IllegalArgumentException(String.format(
                    "Username must match regex: '%s'. Given: '%s'", USERNAME_PATTERN.pattern(), username));
        }

        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException(
                    String.format("Full name must not be null or blank. Given: '%s'", fullName));
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(String.format("Email must not be null or blank. Given: '%s'", email));
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException(
                    String.format("Email must match regex: '%s'. Given: '%s'", EMAIL_PATTERN.pattern(), email));
        }
    }
}
