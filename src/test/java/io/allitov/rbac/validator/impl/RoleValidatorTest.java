package io.allitov.rbac.validator.impl;

import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleValidatorTest {

    private RoleValidator validator;

    @Mock
    private Role role;

    @BeforeEach
    void setUp() {
        validator = new RoleValidator();
    }

    @Test
    @DisplayName("Должен успешно пройти валидацию при корректных данных")
    void shouldPassValidationWhenDataIsValid() {
        when(role.getName()).thenReturn("ADMIN");
        when(role.getDescription()).thenReturn("Full access role");

        assertDoesNotThrow(() -> validator.validate(role));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    @DisplayName("Должен выбросить исключение, если имя роли пустое или null")
    void shouldThrowExceptionWhenNameIsInvalid(String invalidName) {
        when(role.getName()).thenReturn(invalidName);
        when(role.getDescription()).thenReturn("Valid description");

        assertThatThrownBy(() -> validator.validate(role))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Name must not be null or blank")
                .satisfies(ex -> {
                    ValidationException vex = (ValidationException) ex;
                    assertThat(vex.getErrors()).hasSize(1);
                    assertThat(vex.getErrors().get(0)).contains(String.valueOf(invalidName));
                });
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  "})
    @DisplayName("Должен выбросить исключение, если описание пустое или null")
    void shouldThrowExceptionWhenDescriptionIsInvalid(String invalidDescription) {
        when(role.getName()).thenReturn("MANAGER");
        when(role.getDescription()).thenReturn(invalidDescription);

        assertThatThrownBy(() -> validator.validate(role))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Description must not be null or blank");
    }

    @Test
    @DisplayName("Должен собрать все ошибки, если и имя, и описание невалидны")
    void shouldCollectAllErrors() {
        when(role.getName()).thenReturn("");
        when(role.getDescription()).thenReturn(null);

        assertThatThrownBy(() -> validator.validate(role))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    ValidationException vex = (ValidationException) ex;
                    assertThat(vex.getErrors())
                            .hasSize(2)
                            .anyMatch(s -> s.contains("Name"))
                            .anyMatch(s -> s.contains("Description"));
                });
    }
}
