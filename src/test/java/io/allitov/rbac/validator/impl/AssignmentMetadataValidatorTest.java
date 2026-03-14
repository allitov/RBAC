package io.allitov.rbac.validator.impl;

import io.allitov.rbac.model.assignment.AssignmentMetadata;
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
class AssignmentMetadataValidatorTest {

    private AssignmentMetadataValidator validator;

    @Mock
    private AssignmentMetadata metadata;

    @BeforeEach
    void setUp() {
        validator = new AssignmentMetadataValidator();
    }

    @Test
    @DisplayName("Должен успешно пройти валидацию при корректных данных")
    void shouldPassValidationWhenDataIsValid() {
        when(metadata.getAssignedBy()).thenReturn("admin");
        when(metadata.getReason()).thenReturn("Standard role assignment for new employee");

        assertDoesNotThrow(() -> validator.validate(metadata));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t\n"})
    @DisplayName("Должен выбросить исключение, если имя назначившего пустое")
    void shouldFailWhenAssignedByIsBlank(String invalidAssigner) {
        when(metadata.getAssignedBy()).thenReturn(invalidAssigner);
        when(metadata.getReason()).thenReturn("Valid reason");

        assertThatThrownBy(() -> validator.validate(metadata))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Assigner name must not be null or blank")
                .satisfies(ex -> {
                    ValidationException vex = (ValidationException) ex;
                    assertThat(vex.getErrors()).hasSize(1);
                });
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("Должен выбросить исключение, если причина пустая")
    void shouldFailWhenReasonIsBlank(String invalidReason) {
        when(metadata.getAssignedBy()).thenReturn("manager");
        when(metadata.getReason()).thenReturn(invalidReason);

        assertThatThrownBy(() -> validator.validate(metadata))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Reason must not be null or blank");
    }

    @Test
    @DisplayName("Должен собрать все ошибки, если и назначивший, и причина некорректны")
    void shouldCollectAllErrorsWhenBothFieldsAreInvalid() {
        when(metadata.getAssignedBy()).thenReturn("");
        when(metadata.getReason()).thenReturn(null);

        assertThatThrownBy(() -> validator.validate(metadata))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    ValidationException vex = (ValidationException) ex;
                    assertThat(vex.getErrors())
                            .hasSize(2)
                            .anyMatch(s -> s.contains("Assigner name"))
                            .anyMatch(s -> s.contains("Reason"));
                });
    }
}
