package io.allitov.rbac.model.assignment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AssignmentMetadataTest {

    @Test
    @DisplayName("Static now() should create instance with current timestamp")
    void staticNowShouldWork() {
        String user = "operator_7";
        String reason = "Security update";

        LocalDateTime beforeCall = LocalDateTime.now();

        AssignmentMetadata metadata = AssignmentMetadata.now(user, reason);

        assertThat(metadata.assignedBy()).isEqualTo(user);
        assertThat(metadata.reason()).isEqualTo(reason);

        LocalDateTime assignedTime = LocalDateTime.parse(metadata.assignedAt());
        assertThat(assignedTime).isCloseTo(beforeCall, within(1, ChronoUnit.SECONDS));
    }

    @Test
    @DisplayName("format() should match the expected template")
    void formatShouldReturnCorrectString() {
        AssignmentMetadata metadata = new AssignmentMetadata("root", "today", "testing");

        String result = metadata.format();

        assertThat(result).isEqualTo("Assignment Metadata. Assigned by: root, assigned at: today, reason: testing.");
    }
}
