package io.allitov.rbac.repository.assignment;

import io.allitov.rbac.filter.assignment.AssignmentFilter;
import io.allitov.rbac.model.assignment.PermanentAssignment;
import io.allitov.rbac.model.assignment.RoleAssignment;
import io.allitov.rbac.model.assignment.TemporaryAssignment;
import io.allitov.rbac.model.role.Permission;
import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;
import io.allitov.rbac.repository.Repository;
import io.allitov.rbac.repository.role.RoleManager;
import io.allitov.rbac.repository.user.UserManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AssignmentManager implements Repository<RoleAssignment> {

    private static final Map<String, RoleAssignment> ASSIGNMENT_BASE = new HashMap<>();

    private final UserManager userManager;
    private final RoleManager roleManager;

    public AssignmentManager(UserManager userManager, RoleManager roleManager) {
        this.userManager = userManager;
        this.roleManager = roleManager;
    }

    public List<RoleAssignment> findByUser(User user) {
        return ASSIGNMENT_BASE.values().stream()
                .filter(assignment -> assignment.user().equals(user))
                .toList();
    }

    public List<RoleAssignment> findByRole(Role role) {
        return ASSIGNMENT_BASE.values().stream()
                .filter(assignment -> assignment.role().equals(role))
                .toList();
    }

    public List<RoleAssignment> findByFilter(AssignmentFilter filter) {
        return ASSIGNMENT_BASE.values().stream().filter(filter::test).toList();
    }

    public List<RoleAssignment> findAll(AssignmentFilter filter, Comparator<RoleAssignment> sorter) {
        return findByFilter(filter).stream().sorted(sorter).toList();
    }

    public List<RoleAssignment> getActiveAssignments() {
        return ASSIGNMENT_BASE.values().stream()
                .filter(RoleAssignment::isActive)
                .toList();
    }

    public List<RoleAssignment> getExpiredAssignments() {
        return ASSIGNMENT_BASE.values().stream()
                .filter(assignment -> assignment instanceof TemporaryAssignment ta && ta.isExpired())
                .toList();
    }

    public boolean userHasRole(User user, Role role) {
        return findByUser(user).stream()
                .anyMatch(assignment -> assignment.role().equals(role));
    }

    public boolean userHasPermission(User user, String permissionName, String resource) {
        return findByUser(user).stream()
                .anyMatch(assignment -> assignment.role().hasPermission(permissionName, resource));
    }

    public Set<Permission> getUserPermissions(User user) {
        return findByUser(user).stream()
                .flatMap(assignment -> assignment.role().getPermissions().stream())
                .collect(Collectors.toUnmodifiableSet());
    }

    public void revokeAssignment(String assignmentId) {
        ASSIGNMENT_BASE.computeIfPresent(assignmentId, (_, assignment) -> {
            if (assignment instanceof PermanentAssignment pa) {
                pa.revoke();
            }
            return assignment;
        });
    }

    public void extendTemporaryAssignment(String assignmentId, String newExpirationDate) {
        ASSIGNMENT_BASE.computeIfPresent(assignmentId, (_, assignment) -> {
            if (assignment instanceof TemporaryAssignment ta) {
                ta.extend(newExpirationDate);
            }
            return assignment;
        });
    }

    @Override
    public void add(RoleAssignment item) {
        String username = item.user().username();
        if (!userManager.exists(username)) {
            throw new IllegalArgumentException(String.format("Username %s not found", username));
        }
        String roleName = item.role().getName();
        if (!roleManager.exists(roleName)) {
            throw new IllegalArgumentException(String.format("Role %s not found", roleName));
        }

        ASSIGNMENT_BASE.putIfAbsent(item.assignmentId(), item);
    }

    @Override
    public boolean remove(RoleAssignment item) {
        return ASSIGNMENT_BASE.remove(item.assignmentId()) != null;
    }

    @Override
    public Optional<RoleAssignment> findById(String id) {
        return Optional.ofNullable(ASSIGNMENT_BASE.get(id));
    }

    @Override
    public List<RoleAssignment> findAll() {
        return new ArrayList<>(ASSIGNMENT_BASE.values());
    }

    @Override
    public int count() {
        return ASSIGNMENT_BASE.size();
    }

    @Override
    public void clear() {
        ASSIGNMENT_BASE.clear();
    }
}
