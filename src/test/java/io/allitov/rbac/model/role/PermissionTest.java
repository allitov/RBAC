package io.allitov.rbac.model.role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class PermissionTest {

    @Test
    @DisplayName("Should correctly normalize name and resource")
    void shouldNormalizeFields() {
        Permission permission = new Permission("  read_all  ", "  USER_Data  ", "Some desc");

        assertThat(permission.name()).isEqualTo("READ_ALL");
        assertThat(permission.resource()).isEqualTo("user_data");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should throw exception when name is null or blank")
    void shouldThrowExceptionForInvalidName(String name) {
        assertThatThrownBy(() -> new Permission(name, "res", "desc"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name must not be null or blank");
    }

    @Test
    @DisplayName("Should throw exception when name contains spaces")
    void shouldThrowExceptionForSpacesInName() {
        assertThatThrownBy(() -> new Permission("READ DATA", "res", "desc"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name must not contain spaces");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should validate that resource and description are not blank")
    void shouldValidateBlankFields(String invalidInput) {
        assertThatThrownBy(() -> new Permission("READ", invalidInput, "desc"))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Permission("READ", "res", invalidInput))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Format method should return correctly formatted string")
    void formatShouldReturnValidString() {
        Permission permission = new Permission("UPDATE", "document", "Updates docs");
        assertThat(permission.format()).isEqualTo("UPDATE on document: Updates docs");
    }

    @ParameterizedTest
    @CsvSource({"READ, user, true", "REA, us, true", "WRITE, user, false", "READ, admin, false"})
    @DisplayName("Matches method should correctly identify patterns")
    void matchesShouldWorkCorrectly(String namePat, String resPat, boolean expected) {
        Permission permission = new Permission("READ", "user_profile", "Desc");
        assertThat(permission.matches(namePat, resPat)).isEqualTo(expected);
    }
}
