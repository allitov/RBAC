package io.allitov.rbac.model;

public enum AssignmentType {
    PERMANENT("PERMANENT"),
    TEMPORARY("TEMPORARY");

    private final String value;

    AssignmentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
