package io.allitov.rbac.model.validator.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import io.allitov.rbac.model.role.Permission;
import io.allitov.rbac.model.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PermissionValidatorTest {

    private PermissionValidator validator;

    @Mock
    private Permission permission;

    @BeforeEach
    void setUp() {
        validator = new PermissionValidator();
    }

    @Test
    @DisplayName("Должен успешно пройти валидацию при корректных данных")
    void shouldPassValidationWhenDataIsValid() {
        when(permission.getName()).thenReturn("READ_PRIVILEGE");
        when(permission.getResource()).thenReturn("user_service");
        when(permission.getDescription()).thenReturn("Allows reading user data");

        assertDoesNotThrow(() -> validator.validate(permission));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t"})
    @DisplayName("Должен выбросить исключение, если Name, Resource или Description пусты")
    void shouldFailWhenFieldsAreBlank(String invalidValue) {
        when(permission.getName()).thenReturn(invalidValue);
        when(permission.getResource()).thenReturn("res");
        when(permission.getDescription()).thenReturn("desc");

        assertThatThrownBy(() -> validator.validate(permission))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Name must not be null or blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"READ DATA", " WRITE", "DELETE "})
    @DisplayName("Должен выбросить исключение, если имя содержит пробелы")
    void shouldFailWhenNameContainsSpaces(String nameWithSpaces) {
        when(permission.getName()).thenReturn(nameWithSpaces);
        when(permission.getResource()).thenReturn("resource");
        when(permission.getDescription()).thenReturn("description");

        assertThatThrownBy(() -> validator.validate(permission))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Name must not contain spaces");
    }

    @Test
    @DisplayName("Должен собрать все ошибки одновременно")
    void shouldCollectMultipleErrors() {
        when(permission.getName()).thenReturn("INVALID NAME");
        when(permission.getResource()).thenReturn("");
        when(permission.getDescription()).thenReturn(null);

        assertThatThrownBy(() -> validator.validate(permission))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    ValidationException vex = (ValidationException) ex;
                    assertThat(vex.getErrors()).hasSize(3);
                    assertThat(vex.getErrors()).anyMatch(s -> s.contains("spaces"));
                    assertThat(vex.getErrors()).anyMatch(s -> s.contains("Resource"));
                    assertThat(vex.getErrors()).anyMatch(s -> s.contains("Description"));
                });
    }
}
