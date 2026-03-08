package io.allitov.rbac.model.assignment;

import io.allitov.rbac.model.validator.Validator;
import io.allitov.rbac.model.validator.impl.AssignmentMetadataValidator;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AssignmentMetadata {

    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private static final Validator<AssignmentMetadata> VALIDATOR = new AssignmentMetadataValidator();

    private final String assignedBy;
    private final LocalDateTime assignedAt;
    private final String reason;

    public static AssignmentMetadata now(String assignedBy, String reason) {
        AssignmentMetadata newAssignmentMetadata = new AssignmentMetadata(assignedBy, LocalDateTime.now(), reason);
        VALIDATOR.validate(newAssignmentMetadata);
        return newAssignmentMetadata;
    }

    public String format() {
        return "Assignment Metadata. Assigned by: %s, assigned at: %s, reason: %s."
                .formatted(assignedBy, assignedAt, reason);
    }
}
