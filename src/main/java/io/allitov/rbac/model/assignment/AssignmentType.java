package io.allitov.rbac.model.assignment;

import lombok.Getter;

@Getter
public enum AssignmentType {
    PERMANENT("PERMANENT"),
    TEMPORARY("TEMPORARY");

    private final String value;

    AssignmentType(String value) {
        this.value = value;
    }
}
