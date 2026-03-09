package io.allitov.rbac.manager;

import io.allitov.rbac.filter.assignment.AssignmentFilter;
import io.allitov.rbac.model.assignment.PermanentAssignment;
import io.allitov.rbac.model.assignment.RoleAssignment;
import io.allitov.rbac.model.assignment.TemporaryAssignment;
import io.allitov.rbac.model.role.Permission;
import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;
import io.allitov.rbac.repository.impl.AssignmentRepository;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AssignmentManager {

    private final AssignmentRepository assignmentRepository;
    private final UserManager userManager;
    private final RoleManager roleManager;

    public List<RoleAssignment> findByUser(User user) {
        return assignmentRepository.findByUser(user);
    }

    public List<RoleAssignment> findByRole(Role role) {
        return assignmentRepository.findByRole(role);
    }

    public List<RoleAssignment> findByFilter(AssignmentFilter filter) {
        return assignmentRepository.findByFilter(filter);
    }

    public List<RoleAssignment> findAll(AssignmentFilter filter, Comparator<RoleAssignment> sorter) {
        return assignmentRepository.findAll(filter, sorter);
    }

    public List<RoleAssignment> getActiveAssignments() {
        return assignmentRepository.findAllActiveAssignments();
    }

    public List<RoleAssignment> getExpiredAssignments() {
        return assignmentRepository.findAll().stream()
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
        RoleAssignment foundAssignment =
                assignmentRepository.findById(assignmentId).orElse(null);
        if (foundAssignment instanceof PermanentAssignment pa) {
            pa.revoke();
            assignmentRepository.update(pa);
        }
    }

    public void extendTemporaryAssignment(String assignmentId, String newExpirationDate) {
        RoleAssignment foundAssignment = findById(assignmentId);
        if (foundAssignment instanceof TemporaryAssignment ta) {
            ta.extend(LocalDateTime.parse(newExpirationDate));
            assignmentRepository.update(ta);
        }
    }

    public void add(RoleAssignment item) {
        String username = item.user().getUsername();
        if (!userManager.existsByUsername(username)) {
            throw new IllegalArgumentException("Username %s not found".formatted(username));
        }
        String roleName = item.role().getName();
        if (!roleManager.exists(roleName)) {
            throw new IllegalArgumentException("Role %s not found".formatted(roleName));
        }

        assignmentRepository.add(item);
    }

    public boolean remove(RoleAssignment item) {
        return assignmentRepository.remove(item.assignmentId());
    }

    public RoleAssignment findById(String id) {
        return assignmentRepository.findById(id).orElseThrow();
    }

    public List<RoleAssignment> findAll() {
        return assignmentRepository.findAll();
    }

    public int count() {
        return assignmentRepository.count();
    }
}
