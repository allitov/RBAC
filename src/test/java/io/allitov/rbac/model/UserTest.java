package io.allitov.rbac.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class UserTest {

    @Test
    @DisplayName("Should create user when all fields are valid")
    void shouldCreateUserWhenValid() {
        String username = "john_doe123";
        String fullName = "John Doe";
        String email = "john.doe@example.com";

        User user = new User(username, fullName, email);

        assertThat(user.username()).isEqualTo(username);
        assertThat(user.fullName()).isEqualTo(fullName);
        assertThat(user.email()).isEqualTo(email);
    }

    @Test
    @DisplayName("Should trim fields during construction")
    void shouldTrimFields() {
        String username = "  alice_smith  ";
        String fullName = "  Alice Smith  ";
        String email = "  alice@test.com  ";

        User user = new User(username, fullName, email);

        assertThat(user.username()).isEqualTo("alice_smith");
        assertThat(user.fullName()).isEqualTo("Alice Smith");
        assertThat(user.email()).isEqualTo("alice@test.com");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t"})
    @DisplayName("Should throw exception when username is blank")
    void shouldThrowExceptionWhenUsernameIsBlank(String invalidUsername) {
        assertThatThrownBy(() -> new User(invalidUsername, "Full Name", "test@test.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username must not be null or blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "user!", "too_long_username_more_than_20_chars", "no spaces"})
    @DisplayName("Should throw exception when username pattern is invalid")
    void shouldThrowExceptionWhenUsernameIsInvalid(String invalidUsername) {
        assertThatThrownBy(() -> new User(invalidUsername, "Full Name", "test@test.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username must match regex");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t"})
    @DisplayName("Should throw exception when full name is blank")
    void shouldThrowExceptionWhenFullNameIsBlank(String invalidName) {
        assertThatThrownBy(() -> new User("valid_user", invalidName, "test@test.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Full name must not be null or blank");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t"})
    @DisplayName("Should throw exception when email is blank")
    void shouldThrowExceptionWhenEmailIsBlank(String invalidEmail) {
        assertThatThrownBy(() -> new User("valid_user", "Full Name", invalidEmail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email must not be null or blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"plainaddress", "@missing-local.com", "joe@missing-tld", "joe.email.com"})
    @DisplayName("Should throw exception when email format is invalid")
    void shouldThrowExceptionWhenEmailIsInvalid(String invalidEmail) {
        assertThatThrownBy(() -> new User("valid_user", "Full Name", invalidEmail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email must match regex");
    }

    @Test
    @DisplayName("Should return formatted string with all user data")
    void shouldReturnFormattedString() {
        User user = new User("j_doe", "John Doe", "john@example.com");

        String result = user.format();

        assertThat(result).isEqualTo("j_doe (John Doe) <john@example.com>");
    }

    @Test
    @DisplayName("Should verify equality of two identical user records")
    void shouldVerifyEquality() {
        User user1 = new User("bob_builder", "Bob", "bob@build.com");
        User user2 = new User("bob_builder", "Bob", "bob@build.com");

        assertThat(user1).hasSameHashCodeAs(user2).isEqualTo(user2);
    }
}
