package io.allitov.rbac.model.validator.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.allitov.rbac.model.user.User;
import io.allitov.rbac.model.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserValidatorTest {

    private UserValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UserValidator();
    }

    @Test
    @DisplayName("Должен успешно проходить валидацию для корректных данных")
    void shouldPassForValidUser() {
        User user = User.of("johndoe123", "John Doe", "john@example.com");

        validator.validate(user);
    }

    @Test
    @DisplayName("Должен собирать несколько ошибок одновременно")
    void shouldCollectMultipleErrors() {
        assertThatThrownBy(() -> User.of("", " ", "invalid-email"))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    ValidationException vex = (ValidationException) ex;
                    assertThat(vex.getErrors()).hasSizeGreaterThan(2);
                    assertThat(vex.getErrors()).anyMatch(s -> s.contains("Username"));
                    assertThat(vex.getErrors()).anyMatch(s -> s.contains("Full name"));
                    assertThat(vex.getErrors()).anyMatch(s -> s.contains("Email"));
                });
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "user!", "too_long_username_exceeding_twenty_chars", ""})
    @DisplayName("Невалидные username должны вызывать ошибку")
    void shouldFailForInvalidUsernames(String invalidUsername) {
        assertThatThrownBy(() -> User.of(invalidUsername, "Full Name", "test@test.com"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Username");
    }

    @ParameterizedTest
    @ValueSource(strings = {"plainaddress", "#@%^%#$@#$@#.com", "@example.com", "joe.example.com"})
    @DisplayName("Невалидные email должны вызывать ошибку")
    void shouldFailForInvalidEmails(String invalidEmail) {
        assertThatThrownBy(() -> User.of("validUser", "Full Name", invalidEmail))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Email");
    }

    @Test
    @DisplayName("Пустое имя пользователя должно возвращать конкретное сообщение")
    void shouldReturnSpecificMessageForBlankFullName() {
        assertThatThrownBy(() -> User.of("validUser", "  ", "test@test.com"))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    ValidationException vex = (ValidationException) ex;
                    assertThat(vex.getErrors()).anyMatch(s -> s.contains("Full name must not be null or blank"));
                });
    }
}
