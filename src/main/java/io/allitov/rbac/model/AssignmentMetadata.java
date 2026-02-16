package io.allitov.rbac.model;

import java.time.LocalDateTime;

public record AssignmentMetadata(String assignedBy, String assignedAt, String reason) {

    public static AssignmentMetadata now(String assignedBy, String reason) {
        return new AssignmentMetadata(assignedBy, LocalDateTime.now().toString(), reason);
    }

    public String format() {
        return String.format(
                "Assignment Metadata. Assigned by: %s, assigned at: %s, reason: %s.", assignedBy, assignedAt, reason);
    }
}
