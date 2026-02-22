package io.allitov.rbac.model.role;

public record Permission(String name, String resource, String description) {

    public Permission {
        name = normalizeName(name);
        resource = normalizeResource(resource);
        validate(name, resource, description);
    }

    private String normalizeName(String name) {
        return name != null ? name.trim().toUpperCase() : null;
    }

    private String normalizeResource(String resource) {
        return resource != null ? resource.trim().toLowerCase() : null;
    }

    private void validate(String name, String resource, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(String.format("Name must not be null or blank. Given: '%s'", name));
        }

        if (name.contains(" ")) {
            throw new IllegalArgumentException(String.format("Name must not contain spaces. Given: '%s'", name));
        }

        if (resource == null || resource.isBlank()) {
            throw new IllegalArgumentException(
                    String.format("Resource must not be null or blank. Given: '%s'", resource));
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException(
                    String.format("Description must not be null or blank. Given: '%s'", description));
        }
    }

    public String format() {
        return String.format("%s on %s: %s", name, resource, description);
    }

    public boolean matches(String namePattern, String resourcePattern) {
        return name.contains(namePattern) && resource.contains(resourcePattern);
    }
}
